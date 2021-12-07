package com.stv.medinfosys.model.view;

public class UserAccessStatsViewModel {
    private String username;
    private String method;
    private String path;
    private Integer responseStatus;

    public UserAccessStatsViewModel() {
    }

    public UserAccessStatsViewModel(String username, String method, String path, Integer responseStatus) {
        this.username = username;
        this.method = method;
        this.path = path;
        this.responseStatus = responseStatus;
    }

    public String getUsername() {
        return username;
    }

    public UserAccessStatsViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPath() {
        return path;
    }

    public UserAccessStatsViewModel setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public UserAccessStatsViewModel setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "UserAccessStatsViewModel{" +
                "username='" + username + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
