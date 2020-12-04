package com.COMP3095.gbc_pay.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @NotBlank(message = "The subject cannot be empty.")
    @Size(max = 30, message = "The subject has to be less than 30 characters.")
    private String subject;

    @Lob
    @NotBlank(message = "The message cannot be empty.")
    @Size(max = 225, message = "The message has to be less than 225 characters.")
    private String messageBody;

    @Lob
    @NotBlank(message = "The message cannot be empty.")
    @Size(max = 225, message = "The message has to be less than 225 characters.")
    private String replyMessageBody;

    private boolean read;

    private boolean hasReply;

    private LocalDateTime sentDateTime;

    @ManyToMany(mappedBy = "messages", fetch = FetchType.EAGER)
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

    public String getReplyMessageBody() {
        return replyMessageBody;
    }

    public void setReplyMessageBody(String replyMessageBody) {
        this.replyMessageBody = replyMessageBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return id != null ? id.equals(message.id) : message.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public boolean isHasReply() {
        return hasReply;
    }

    public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }
}
