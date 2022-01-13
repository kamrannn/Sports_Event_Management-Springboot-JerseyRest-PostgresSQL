package org.management.SportEvent.dao;

import org.management.SportEvent.model.Event;
import org.management.SportEvent.model.EventEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventDao extends JpaRepository<Event, Integer> {
    Optional<Event> findByEventName(EventEnum eventName);
}
