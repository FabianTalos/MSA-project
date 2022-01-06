package com.example.publictransportationapp.model;

public class GroupDirectionModel implements ItemInterface {
    public String direction;

    public GroupDirectionModel(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
