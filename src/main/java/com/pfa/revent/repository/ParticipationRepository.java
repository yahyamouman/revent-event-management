package com.pfa.revent.repository;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.Participation;
import com.pfa.revent.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation,Long> {
    List<Participation> findParticipationByParticipatedEventEventId(long eventId);
    List<Participation> findParticipationByParticipant(User participant);
    List<Participation> findParticipationByParticipationType(String participationType);

    //Warning There could be more than one result if the update feature fails
    @Query("SELECT p FROM Participation p WHERE p.participant.userId = ?1 AND p.participatedEvent.eventId = ?2")
    Optional<Participation> getParticipationByUserAndEvent(long userId, long eventId);

    @Query("SELECT p FROM Participation p WHERE p.participant.userId = ?1")
    Page<Participation> getAllParticipationsByParticipant(long userId, Pageable pageable);

    @Query("SELECT p FROM Participation p WHERE p.participatedEvent.eventId = ?1")
    Page<Participation> getAllParticipationsByParticipatedEvent(long eventId, Pageable pageable);

    @Query("SELECT p FROM Participation p WHERE p.participatedEvent.eventId = ?1")
    List<Participation> getAllParticipationsByParticipatedEvent2(long eventId);
}
