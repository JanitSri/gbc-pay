package com.COMP3095.formvalidiation.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Add subject line before sending a messgae")
    private String subject;

    @Lob
    @NotBlank(message = "Cannot send an empty email")
    private String messageBody;

    private boolean read;

    private LocalDateTime sentDateTime;

    @ManyToMany(mappedBy = "messages")
    private Set<Profile> profiles = new HashSet<>();

    public Message() {
    }

    public Message(String subject, String messageBody, boolean read) {
        this.subject = subject;
        this.messageBody = messageBody;
        this.read = read;
        this.sentDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
