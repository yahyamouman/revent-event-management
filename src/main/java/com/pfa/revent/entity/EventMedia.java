package com.pfa.revent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

@Entity
@Table(name="EVENT_MEDIA")
public class EventMedia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event mediatedEvent;

    private String Description;
    private Blob eventMedia;

    public EventMedia() {
        super();
    }


    public EventMedia(long eventMediaId, Event mediatedEvent, Blob eventMedia, String description) {
        this.eventMediaId = eventMediaId;
        this.mediatedEvent = mediatedEvent;
        this.eventMedia = eventMedia;
        Description = description;
    }

    public long getEventMediaId() {
        return eventMediaId;
    }

    public void setEventMediaId(long eventMediaId) {
        this.eventMediaId = eventMediaId;
    }

    public Event getMediatedEvent() {
        return mediatedEvent;
    }

    public void setMediatedEvent(Event mediatedEvent) {
        this.mediatedEvent = mediatedEvent;
    }

    public Blob getEventMedia() {
        return eventMedia;
    }

    public void setEventMedia(Blob eventMedia) {
        this.eventMedia = eventMedia;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
