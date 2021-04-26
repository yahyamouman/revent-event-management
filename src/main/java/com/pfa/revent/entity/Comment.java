package com.pfa.revent.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Entity
@Table(name="comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id")
    private User commentOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event commentedEvent;

    private String comment;

    @CreationTimestamp
    private Timestamp commentDate;

    public Comment() {
        super();
    }


    public Comment(long commentId, User commentOwner, Event commentedEvent, String comment, Timestamp commentDate) {
        this.commentId = commentId;
        this.commentOwner = commentOwner;
        this.commentedEvent = commentedEvent;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public User getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(User commentOwner) {
        this.commentOwner = commentOwner;
    }

    public Event getCommentedEvent() {
        return commentedEvent;
    }

    public void setCommentedEvent(Event commentedEvent) {
        this.commentedEvent = commentedEvent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }
}
