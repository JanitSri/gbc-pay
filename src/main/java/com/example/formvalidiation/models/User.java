package com.example.formvalidiation.models;


import com.example.formvalidiation.validation.PasswordMatches;

import javax.persistence.*;
import javax.validation.constraints.*;

@PasswordMatches(first = "password", second = "confirmPassword", message = "The password fields must match")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 25, message = "First Name must between 2 and 25 characters")
    private String firstName;

    @Size(min = 2, max = 25, message = "Last Name must between 2 and 25 characters")
    private String lastName;

    @Size(min = 2, max = 25, message = "Address must between 2 and 25 characters")
    private String address;

    @NotBlank(message = "Email cannot be Empty")
    @Email(message = "Not valid Email")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "Password must be more than 8 characters, no whitespaces and must contain each of the following; uppercase, lowercase and digit (0-9).")
    private String password;

    @Transient
    @NotBlank(message = "Confirm Password Cannot be Empty")
    private String confirmPassword;

    @Transient
    @NotNull
    @AssertTrue(message = "Must agree to terms of service.")
    private boolean agreedToTerms;

    private boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "verificatiotoken_id")
    private VerificationToken verificationToken;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "emailtoken_id")
    private EmailToken emailToken;

    public User() {
    }

    public User(String firstName, String lastName, String address, String password, String email, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(VerificationToken validationVerificationToken) {
        this.verificationToken = validationVerificationToken;
    }

    public EmailToken getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(EmailToken emailToken) {
        this.emailToken = emailToken;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

