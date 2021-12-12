package com.stv.medinfosys.model.binding;

import javax.validation.constraints.Pattern;

public class ChangeUsernameAndPasswordBindingModel {
    private String username;
    private String confirmUsername;
    private String password;
    private String confirmPassword;

    public ChangeUsernameAndPasswordBindingModel() {
    }

    @Pattern(regexp = "|\\w{4,20}")
    public String getUsername() {
        return username;
    }

    public ChangeUsernameAndPasswordBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    @Pattern(regexp = "|\\w{4,20}")
    public String getPassword() {
        return password;
    }

    public ChangeUsernameAndPasswordBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    @Pattern(regexp = "|\\w{4,20}")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public ChangeUsernameAndPasswordBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    @Pattern(regexp = "|\\w{4,20}")
    public String getConfirmUsername() {
        return confirmUsername;
    }

    public ChangeUsernameAndPasswordBindingModel setConfirmUsername(String confirmUsername) {
        this.confirmUsername = confirmUsername;
        return this;
    }
}
