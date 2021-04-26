package com.pfa.revent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Entity
public class EventTag implements Serializable {
    @Id
    @GeneratedValue
    private long eventTagId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event taggedEvent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Tag usedTag;

    public EventTag() {
        super();
    }

    public long getEventTagId() {
        return eventTagId;
    }

    public void setEventTagId(long eventTagId) {
        this.eventTagId = eventTagId;
    }

    public Event getTaggedEvent() {
        return taggedEvent;
    }

    public void setTaggedEvent(Event taggedEvent) {
        this.taggedEvent = taggedEvent;
    }

    public Tag getUsedTag() {
        return usedTag;
    }

    public void setUsedTag(Tag usedTag) {
        this.usedTag = usedTag;
    }

    public EventTag(long eventTagId, Event taggedEvent, Tag usedTag) {
        this.eventTagId = eventTagId;
        this.taggedEvent = taggedEvent;
        this.usedTag = usedTag;
    }
}
