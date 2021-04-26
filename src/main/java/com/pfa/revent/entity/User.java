package com.pfa.revent.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private long userId;
    private String firstName;
    private String lastName;
    //pattern
    private String password;
    @Transient
    private String passwordConfirm;
    @Email
    @Column(unique = true)
    private String email;
    private Date birthdate;
    private String userType;

    @CreationTimestamp
    private Timestamp registrationDate;

    private Blob avatarImage;
    private String avatarName;

    //Spring security//////////////////////////////////
    @Column(unique = true)
    private String username;
    private String userAccessPrivilege;
    private static final boolean active = true;
    private String roles;
    ///////////////////////////////////////////////////

    //Moderator////////////////////////////////////////
    private int maxEditors;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "parentModerator",fetch = FetchType.LAZY)
    private Collection<User> editors;
    ///////////////////////////////////////////////////

    //Editor///////////////////////////////////////////
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private User parentModerator;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "organizer")
    private Collection<Event> events;
    ///////////////////////////////////////////////////


    //Viewer///////////////////////////////////////////
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "commentOwner")
    private Collection<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "participant")
    private Collection<Participation> participations;
    ///////////////////////////////////////////////////

    public User() {
        super();
    }

    public User(long userId, String firstName, String lastName, String password,
                String passwordConfirm, @Email String email, Date birthdate, String userType,
                Timestamp registrationDate, Blob avatarImage, String avatarName, String username,
                String userAccessPrivilege, String roles, int maxEditors, Collection<User> editors,
                User parentModerator, Collection<Event> events, Collection<Comment> comments,
                Collection<Participation> participations) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
        this.birthdate = birthdate;
        this.userType = userType;
        this.registrationDate = registrationDate;
        this.avatarImage = avatarImage;
        this.avatarName = avatarName;
        this.username = username;
        this.userAccessPrivilege = userAccessPrivilege;
        this.roles=roles;
        this.maxEditors = maxEditors;
        this.editors = editors;
        this.parentModerator = parentModerator;
        this.events = events;
        this.comments = comments;
        this.participations = participations;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Blob getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(Blob avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAccessPrivilege() {
        return userAccessPrivilege;
    }

    public void setUserAccessPrivilege(String userAccessPrivilege) {
        this.userAccessPrivilege = userAccessPrivilege;
    }

    public static boolean isActive() {
        return active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getMaxEditors() {
        return maxEditors;
    }

    public void setMaxEditors(int maxEditors) {
        this.maxEditors = maxEditors;
    }

    public Collection<User> getEditors() {
        return editors;
    }

    public void setEditors(Collection<User> editors) {
        this.editors = editors;
    }

    public User getParentModerator() {
        return parentModerator;
    }

    public void setParentModerator(User parentModerator) {
        this.parentModerator = parentModerator;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public void setEvents(Collection<Event> events) {
        this.events = events;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Collection<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(Collection<Participation> participations) {
        this.participations = participations;
    }

}