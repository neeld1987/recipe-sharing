package com.example.recipesharing.respository;

import com.example.recipesharing.model.Recipe;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> searchRecipeByUserName(String userName);

    List<Recipe> searchRecipeByRecipeTitle(String recipeTitle);
}
