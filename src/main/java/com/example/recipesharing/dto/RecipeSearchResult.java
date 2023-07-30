package com.example.recipesharing.dto;

import com.example.recipesharing.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class RecipeSearchResult {

    private String title;

    private String description;

    private List<Ingredient> ingredients = new ArrayList<>();

    private String instructions;

    private int servings;

    private String userName;

    public RecipeSearchResult(){}



}
