package com.stv.medinfosys.model.service;

public class UserAccessStatsServiceModel {
    private String username;
    private String method;
    private String path;
    private Integer responseStatus;

    public UserAccessStatsServiceModel() {
    }

    public UserAccessStatsServiceModel(String username, String method, String path, Integer responseStatus) {
        this.username = username;
        this.method = method;
        this.path = path;
        this.responseStatus = responseStatus;
    }

    public String getUsername() {
        return username;
    }

    public UserAccessStatsServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPath() {
        return path;
    }

    public UserAccessStatsServiceModel setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public UserAccessStatsServiceModel setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "UserAccessStatsServiceModel{" +
                "username='" + username + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
