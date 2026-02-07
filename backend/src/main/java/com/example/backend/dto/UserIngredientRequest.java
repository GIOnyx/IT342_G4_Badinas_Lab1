package com.example.backend.dto;

public class UserIngredientRequest {
    private Integer spoonacularId;
    private String name;
    private String aisle;

    public UserIngredientRequest() {}

    public UserIngredientRequest(Integer spoonacularId, String name, String aisle) {
        this.spoonacularId = spoonacularId;
        this.name = name;
        this.aisle = aisle;
    }

    public Integer getSpoonacularId() {
        return spoonacularId;
    }

    public void setSpoonacularId(Integer spoonacularId) {
        this.spoonacularId = spoonacularId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }
}
