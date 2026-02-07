package com.example.backend.controller;

import com.example.backend.service.SpoonacularService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spoon")
public class SpoonacularController {
    private final SpoonacularService service;

    public SpoonacularController(SpoonacularService service) {
        this.service = service;
    }

    @GetMapping(value = "/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchIngredients(
            @RequestParam String query,
            @RequestParam(required = false) Integer number) {
        String body = service.searchIngredients(query, number);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping(value = "/ingredients/autocomplete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> autocompleteIngredients(
            @RequestParam String query,
            @RequestParam(required = false) Integer number,
            @RequestParam(required = false) Boolean metaInformation) {
        String body = service.autocompleteIngredients(query, number, metaInformation);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping(value = "/recipes-by-ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recipesByIngredients(@RequestParam String ingredients, @RequestParam(required = false) Integer number) {
        String body = service.findRecipesByIngredients(ingredients, number);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping(value = "/recipes/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchRecipes(@RequestParam String query, @RequestParam(required = false) Integer number) {
        String body = service.searchRecipes(query, number);
        return ResponseEntity.ok().body(body);
    }
}
