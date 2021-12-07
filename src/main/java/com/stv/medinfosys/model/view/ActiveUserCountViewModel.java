package com.stv.medinfosys.model.view;

public class ActiveUserCountViewModel {
    private int activeUsersCount;

    public ActiveUserCountViewModel() {
    }

    public ActiveUserCountViewModel(int activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
    }

    public int getActiveUsersCount() {
        return activeUsersCount;
    }

    public ActiveUserCountViewModel setActiveUsersCount(int activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
        return this;
    }
}
