package com.example.recipesharing.dto;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearchResult {

    private String title;

    private String description;

    private List<IngredientDTO> ingredients = new ArrayList<>();

    private String instructions;

    private int servings;

    private String createdBy;
}
