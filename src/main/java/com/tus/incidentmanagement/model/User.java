package com.tus.incidentmanagement.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private String roleName;

    public User() {}

    public User(Long id, String username, String password, String roleName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleName = roleName;
    }


}
