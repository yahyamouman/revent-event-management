package com.pfa.revent.repository;


import com.pfa.revent.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByStartDateBetween(Date minDate, Date maxDate);
    List<Event> findByOrganizer(Event organizer);
    List<Event> findByCity(String city);
    List<Event> findByEventTitleContaining(String infixTitle);
 /*   @Query("SELECT e FROM Event e GROUP BY e.organizer.userId")
    Page<Event> findAllGroupByOrganizerId(Pageable pageable);*/
    @Query("SELECT e FROM Event e WHERE e.organizer.userId=?1")
    Page<Event> findEventsByOrganizerId(long organizerId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.organizer.userId=?1 OR e.organizer.parentModerator.userId=?1 GROUP BY e.organizer.userId ")
    Page<Event> findSubEventsOfId(long id, Pageable pageable);

    /*ORDER BY e.startDate DESC*/
    @Query("SELECT e FROM Event e WHERE e.eventTitle LIKE %?1% OR e.organizer.username LIKE %?1%")
    Page<Event> findEventByEventTitleOrUsername(String search, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventType=?1")
    Page<Event> findEventByType(String type, Pageable pageable);
}