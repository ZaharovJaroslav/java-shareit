package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {
   /* @Query("SELECT r FROM requests r"
            + "WHERE r.requester_id = :userId")*/
   Collection<ItemRequest> findByCreatorIdIsNot(Long userId);

   Collection<ItemRequest> findByCreatorIdOrderByCreatedDesc(Long userId);
}
