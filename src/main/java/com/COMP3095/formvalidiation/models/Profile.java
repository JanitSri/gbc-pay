package com.COMP3095.formvalidiation.models;

import java.time.LocalDateTime;

public class Profile {

    private Long id;
    private String email;
    private boolean emailVerified;
    private String password;
    private LocalDateTime lastLogin;
    private LocalDateTime lastUpdate;
}
