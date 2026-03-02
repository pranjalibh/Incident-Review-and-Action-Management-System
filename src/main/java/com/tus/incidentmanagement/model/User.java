package com.tus.incidentmanagement.model;

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

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
