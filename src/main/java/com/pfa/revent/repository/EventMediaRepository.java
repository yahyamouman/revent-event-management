package com.pfa.revent.repository;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.EventMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia,Long> {
    List<EventMedia> findEventMediaByMediatedEvent(Event mediatedEvent);
    List<EventMedia> getEventMediaBymediatedEvent(Long eventId);
}
