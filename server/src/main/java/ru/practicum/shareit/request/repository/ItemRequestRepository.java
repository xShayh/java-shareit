package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Collection<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(long requestId);

    Collection<ItemRequest> findAllByRequesterIdNotOrderByCreatedDesc(long requestId);
}