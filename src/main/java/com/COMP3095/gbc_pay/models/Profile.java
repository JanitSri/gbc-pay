package com.COMP3095.gbc_pay.models;

import com.COMP3095.gbc_pay.validation.PasswordMatches;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PasswordMatches(first = "password", second = "confirmPassword", message = "Password fields must match")
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Not valid Email")
    @NotBlank(message = "Email Cannot be Empty")
    private String email;

    private boolean emailVerified;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,12}$",
            message = "Password must be between 6-12 characters, contain at least 1 uppercase letter and " +
                    "contain at least 1 special character")
    private String password;

    @Transient
    @NotBlank(message = "Confirm Password cannot be empty")
    private String confirmPassword;

    @Transient
    @AssertTrue(message = "Must agree to terms of service")
    private boolean agreedToTerms;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "profile_role", // join table for user and role
            joinColumns = @JoinColumn(name = "profile_id"), // owner side
            inverseJoinColumns = @JoinColumn(name = "role_id") // other side
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private Set<Credit> credits = new HashSet<Credit>();

    @OneToOne(cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private Set<PasswordResetToken> passwordResetTokens = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "profile_message", // join table for user and role
            joinColumns = @JoinColumn(name = "profile_id"), // owner side
            inverseJoinColumns = @JoinColumn(name = "message_id") // other side
    )
    private List<Message> messages = new ArrayList<>();

    public Profile() {
    }

    public Profile(String email, boolean emailVerified, String password) {
        this.email = email;
        this.emailVerified = emailVerified;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Credit> getCredits() {
        return credits;
    }

    public void setCredits(Set<Credit> credits) {
        this.credits = credits;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(VerificationToken verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Set<PasswordResetToken> getPasswordResetTokens() {
        return passwordResetTokens;
    }

    public void setPasswordResetTokens(Set<PasswordResetToken> passwordResetTokens) {
        this.passwordResetTokens = passwordResetTokens;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return id != null ? id.equals(profile.id) : profile.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
