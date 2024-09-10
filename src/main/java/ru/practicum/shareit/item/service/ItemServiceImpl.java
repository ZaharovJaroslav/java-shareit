package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDTO addNewItem(long userId, ItemDTO item) {
        log.debug("addNewItem({}, {})", userId,item);
        itemValidation(item);
        User user = userService.getUserById(userId);

        Item newItem = ItemMapper.mapToItem(item);
        newItem.setOwnerId(user.getId());
        repository.save(newItem);
        return ItemMapper.toItemDto(newItem);
      //  return repository.findById(repository.saveItem(newItem));
    }

    public void itemValidation(ItemDTO item) {
        log.info("itemValidation({})", item);

        if (item.getAvailable() == null) {
            throw new ValidationException("При добавлении нового инструмента он должен быть доступен");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название инструмента не задано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание инструмента не задано");
        }
    }

    @Override
    public ItemDTO updateItem(long itemId, long userId, UpdateItemRequest request) {
        log.debug("updateItem({}, {}, {})",itemId, userId, request);
        Item updatedItem = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("инструмент не найден"));
        if (updatedItem.getOwnerId() != userId) {
            throw new NotFoundException("У пользователя нет такого инcтрумента");
        }
        ItemMapper.updateItemFields(updatedItem, request);

        return ItemMapper.toItemDto(repository.findById(itemId).get());
    }

     @Override
    public ItemDTO findItemById(Long itemId, Long userId) {
         ItemDTO result;
         Item item = repository.findById(itemId)
                 .orElseThrow(() -> new NotFoundException(String.format("Item with ID = %d not found.", itemId)));
         result = ItemMapper.toItemDto(item);
         if (Objects.equals(item.getOwnerId(), userId)) {
             updateBookings(result);
         }
        List<Comment> comments = commentRepository.findAllByItemId(result.getId());
        result.setComments(CommentMapper.toDtoList(comments));
         return result;
     }
         public ItemDTO updateBookings(ItemDTO itemDto) {
             LocalDateTime now = LocalDateTime.now();
             List<Booking> bookings = bookingRepository.findBookingsItem(itemDto.getId());
             Booking lastBooking = bookings.stream()
                     .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED.REJECTED)))
                     .filter(obj -> obj.getStart().isBefore(now))
                     .min((obj1, obj2) -> obj2.getStart().compareTo(obj1.getStart())).orElse(null);
             Booking nextBooking = bookings.stream()
                     .filter(obj -> !(obj.getStatus().equals(BookingStatus.REJECTED)))
                     .filter(obj -> obj.getStart().isAfter(now))
                     .min(Comparator.comparing(Booking::getStart)).orElse(null);
             if (lastBooking != null) {
                 itemDto.setLastBooking(BookingMapper.toItemBookingDto(lastBooking));
             }
             if (nextBooking != null) {
                 itemDto.setNextBooking(BookingMapper.toItemBookingDto(nextBooking));
             }
             return itemDto;
         }


         @Override
    public Item getItemById(long itemId) {
        log.debug("getItemById({})", itemId);
       // ItemDTO result;

        Item item = repository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("Инструмента с таким id не существует"));



      //  result = ItemMapper.toItemDTO(item);
        //return item;
return item;

      //  return result;
    }

    @Override
    public  Collection<ItemDTO> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        userService.getUserById(userId);

        List<ItemDTO> item = repository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();

        List<ItemDTO> list = new ArrayList<>();
        item.stream().map(this::updateBookings).forEach(i -> {
            CommentMapper.toDtoList(commentRepository.findAllByItemId(i.getId()));
            list.add(i);
        });
        return  list;
    }

    @Override
    public Collection<ItemDTO> findItemByNameOrDescription(String text) {
        log.debug("findItemByNameOrDescription({})", text);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

      Collection<ItemDTO> result = repository.searchAvailableItems(text).stream()
                .map(ItemMapper::toItemDto)
              .filter(itemDTO -> itemDTO.getAvailable().equals(true))
              .toList();
        return result;


    }

    @Override
    public Long findOwnerId(Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID = %d not found.", itemId)))
                .getOwnerId();
    }

@Override
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID = %d not found.", itemId)));
        User user = userService.getUserById(userId);
        List<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        log.info(bookings.toString());
        if (!bookings.isEmpty() && bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
            Comment comment = CommentMapper.toComment(commentDto);
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setCreated(LocalDateTime.now());
            return CommentMapper.toDto(commentRepository.save(comment));
        } else {
            throw new NotAvailableException(String.format("Booking for user with ID = %d and item with ID = %d not found.", userId, itemId));
        }
    }
}

