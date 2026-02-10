package com.example.backend.dto;

public class UserRecipeRequest {
    private Integer spoonacularRecipeId;
    private String title;
    private String image;
    private Integer usedIngredients;
    private Integer missedIngredients;

    public UserRecipeRequest() {}

    public UserRecipeRequest(Integer spoonacularRecipeId, String title, String image, Integer usedIngredients, Integer missedIngredients) {
        this.spoonacularRecipeId = spoonacularRecipeId;
        this.title = title;
        this.image = image;
        this.usedIngredients = usedIngredients;
        this.missedIngredients = missedIngredients;
    }

    public Integer getSpoonacularRecipeId() {
        return spoonacularRecipeId;
    }

    public void setSpoonacularRecipeId(Integer spoonacularRecipeId) {
        this.spoonacularRecipeId = spoonacularRecipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(Integer usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public Integer getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(Integer missedIngredients) {
        this.missedIngredients = missedIngredients;
    }
}
