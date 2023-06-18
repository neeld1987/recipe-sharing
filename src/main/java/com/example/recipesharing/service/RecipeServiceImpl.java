package com.example.recipesharing.service;

import com.example.recipesharing.dto.IngredientDTO;
import com.example.recipesharing.dto.RecipeDTO;
import com.example.recipesharing.model.Ingredient;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.RecipeCreator;
import com.example.recipesharing.model.constants.IngredientUnit;
import com.example.recipesharing.respository.RecipeRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public Recipe saveRecipe(RecipeDTO recipeDTO, RecipeCreator recipeCreator) {

        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setServings(recipeDTO.getServings());
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setUser(recipeCreator);

        for(IngredientDTO ingredientDTO : recipeDTO.getIngredients()){
            Ingredient ingredients = new Ingredient();
            ingredients.setType(ingredientDTO.getType());
            ingredients.setUnit(ingredientDTO.getUnit());
            ingredients.setValue(ingredientDTO.getValue());
            recipe.addIngredient(ingredients);
            ingredients.setRecipe(recipe);
        }
        /*
        Ingredient ingredients1 = new Ingredient();
        ingredients1.setType("egg");
        ingredients1.setUnit(IngredientUnit.pc);
        ingredients1.setValue(8);

        Ingredient ingredients2 = new Ingredient();
        ingredients2.setType("spaghetti");
        ingredients2.setUnit(IngredientUnit.g);
        ingredients2.setValue(500);



        Recipe recipe = new Recipe();
        recipe.setDescription("Just make Spaghetti Carbonara");
        recipe.setInstructions("DIY");
        recipe.setServings(4);
        recipe.setUser(existingUser);
        recipe.setTitle("Spaghetti Carbonara");
        recipe.addIngredient(ingredients1);
        recipe.addIngredient(ingredients2);

        ingredients1.setRecipe(recipe);
        ingredients2.setRecipe(recipe);

         */

        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe;

    }

    @Override
    public List<Recipe> searchRecipeByRecipeTitle(String recipeTitle) {
        return recipeRepository.searchRecipeByRecipeTitle(recipeTitle);
    }

    @Override
    public List<Recipe> searchRecipeByUserName(String userName) {
        return recipeRepository.searchRecipeByUserName(userName);
    }
}
