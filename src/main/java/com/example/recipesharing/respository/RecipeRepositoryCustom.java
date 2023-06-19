package com.example.recipesharing.respository;

import com.example.recipesharing.model.Recipe;

import java.util.List;

/**
 * A custom repository to handle Recipe search
 */
public interface RecipeRepositoryCustom {

    List<Recipe> searchRecipeByUserName(String userName);

    List<Recipe> searchRecipeByRecipeTitle(String recipeTitle);
}
