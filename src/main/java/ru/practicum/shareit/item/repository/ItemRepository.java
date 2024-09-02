package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    Collection<Item> findAllByOwnerId(Long userId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Collection<Item> findItemByNameOrDescription(String text);

  /*  @Query("select i from Item i" +
            "where u.id like"
    )
    Collection<Item> getAllItemsUser(long userId);*/


  /* long saveItem(Item item);

    void updateItem(Item item);

    Optional<Item> getItemById(long itemId);

    Collection<Item> getAllItemsUser(long userId);

    Collection<Item> findItemByNameOrDescription(String text);*/
}
