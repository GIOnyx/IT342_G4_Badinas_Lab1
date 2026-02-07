package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

@Service
public class SpoonacularService {
    private final String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public SpoonacularService(@Value("${spoonacular.api.key:${SPOONACULAR_API_KEY:}}") String apiKey) {
        this.apiKey = apiKey;
    }

    private void ensureKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Spoonacular API key is not configured. Set environment variable SPOONACULAR_API_KEY or property spoonacular.api.key");
        }
    }

    public String searchIngredients(String query, Integer number) {
        ensureKey();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.spoonacular.com/food/ingredients/search")
                .queryParam("query", query)
                .queryParam("number", number == null ? 20 : number)
                .queryParam("apiKey", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, String.class);
    }

    public String autocompleteIngredients(String query, Integer number, Boolean metaInformation) {
        ensureKey();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.spoonacular.com/food/ingredients/autocomplete")
                .queryParam("query", query)
                .queryParam("number", number == null ? 20 : number)
                .queryParam("metaInformation", metaInformation == null ? true : metaInformation)
                .queryParam("apiKey", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, String.class);
    }

    public String findRecipesByIngredients(String ingredients, Integer number) {
        ensureKey();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.spoonacular.com/recipes/findByIngredients")
                .queryParam("ingredients", ingredients)
                .queryParam("number", number == null ? 5 : number)
                .queryParam("apiKey", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, String.class);
    }

    public String searchRecipes(String query, Integer number) {
        ensureKey();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.spoonacular.com/recipes/complexSearch")
                .queryParam("query", query)
                .queryParam("number", number == null ? 12 : number)
                .queryParam("addRecipeInformation", true)
                .queryParam("apiKey", apiKey)
                .toUriString();
        return restTemplate.getForObject(url, String.class);
    }
}
