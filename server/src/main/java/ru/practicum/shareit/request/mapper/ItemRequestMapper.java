package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .requestor(UserMapper.toUserDto(request.getCreator()))
                .items(request.getItems() != null ? request.getItems()
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto) {
        return ItemRequest.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .creator(UserMapper.toUser(requestDto.getRequestor()))
                .created(requestDto.getCreated())
                .build();
    }

}
