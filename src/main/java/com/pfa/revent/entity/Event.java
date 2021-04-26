package com.pfa.revent.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name="event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;
    private String eventTitle;
    private String eventType;
    private Date startDate;
    private Date endDate;
    private String city;
    private String region;
    private int zipCode;
    private String street;
    private String otherAddressInformation;
    private String description;
    private String status;
    private long thumbnailMediaId;
    private String thumbnailName;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "mediatedEvent",fetch = FetchType.LAZY)
    private Collection<EventMedia> eventMediaCollection;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "participatedEvent",fetch = FetchType.LAZY)
    private Collection<Participation> participations;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "commentedEvent",fetch = FetchType.LAZY)
    private Collection<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "taggedEvent",fetch = FetchType.LAZY)
    private Collection<EventTag> eventTags;

    public Event() {
        super();
    }


    public Event(long eventId, User organizer, String eventTitle, String eventType, Date startDate, Date endDate, String city, String region, int zipCode, String street, String otherAddressInformation, String description, String status, long thumbnailMediaId, String thumbnailName, Collection<EventMedia> eventMediaCollection, Collection<Participation> participations, Collection<Comment> comments, Collection<EventTag> eventTags) {
        this.eventId = eventId;
        this.organizer = organizer;
        this.eventTitle = eventTitle;
        this.eventType = eventType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.region = region;
        this.zipCode = zipCode;
        this.street = street;
        this.otherAddressInformation = otherAddressInformation;
        this.description = description;
        this.status = status;
        this.thumbnailMediaId = thumbnailMediaId;
        this.thumbnailName = thumbnailName;
        this.eventMediaCollection = eventMediaCollection;
        this.participations = participations;
        this.comments = comments;
        this.eventTags = eventTags;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOtherAddressInformation() {
        return otherAddressInformation;
    }

    public void setOtherAddressInformation(String otherAddressInformation) {
        this.otherAddressInformation = otherAddressInformation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getThumbnailMediaId() {
        return thumbnailMediaId;
    }

    public void setThumbnailMediaId(long thumbnailMediaId) {
        this.thumbnailMediaId = thumbnailMediaId;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public Collection<EventMedia> getEventMediaCollection() {
        return eventMediaCollection;
    }

    public void setEventMediaCollection(Collection<EventMedia> eventMediaCollection) {
        this.eventMediaCollection = eventMediaCollection;
    }

    public Collection<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(Collection<Participation> participations) {
        this.participations = participations;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Collection<EventTag> getEventTags() {
        return eventTags;
    }

    public void setEventTags(Collection<EventTag> eventTags) {
        this.eventTags = eventTags;
    }

}
