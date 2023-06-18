package com.example.recipesharing.service;

import com.example.recipesharing.dto.RecipeDTO;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.RecipeCreator;

import java.util.List;

public interface RecipeService {

    public Recipe saveRecipe(RecipeDTO recipe, RecipeCreator recipeCreator);

    public List<Recipe> searchRecipeByRecipeTitle(String recipeTitle);

    public List<Recipe> searchRecipeByUserName(String userName);

}
