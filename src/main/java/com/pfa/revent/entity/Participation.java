package com.pfa.revent.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="participation")
public class Participation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long participationId;

    private String participationType;

    @CreationTimestamp
    private Timestamp participationDate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id")
    private User participant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event participatedEvent;

    public Participation() {
        super();
    }

    public Participation(long participationId, User participant, Event participatedEvent, String participationType, Timestamp participationDate) {
        this.participationId = participationId;
        this.participant = participant;
        this.participatedEvent = participatedEvent;
        this.participationType = participationType;
        this.participationDate = participationDate;
    }

    public Participation(User user, Event event, String participationType) {
    }

    public long getParticipationId() {
        return participationId;
    }

    public void setParticipationId(long participationId) {
        this.participationId = participationId;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public Event getParticipatedEvent() {
        return participatedEvent;
    }

    public void setParticipatedEvent(Event participatedEvent) {
        this.participatedEvent = participatedEvent;
    }

    public String getParticipationType() {
        return participationType;
    }

    public void setParticipationType(String participationType) {
        this.participationType = participationType;
    }

    public Timestamp getParticipationDate() {
        return participationDate;
    }

    public void setParticipationDate(Timestamp participationDate) {
        this.participationDate = participationDate;
    }
}
