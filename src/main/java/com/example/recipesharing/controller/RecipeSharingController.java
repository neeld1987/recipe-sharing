package com.example.recipesharing.controller;

import com.example.recipesharing.dto.RecipeDTO;
import com.example.recipesharing.dto.RecipeSearchResult;
import com.example.recipesharing.dto.UserDTO;
import com.example.recipesharing.exception.EntityAlreadyExistException;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.RecipeCreator;
import com.example.recipesharing.service.RecipeService;
import com.example.recipesharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@EnableWebSecurity
public class RecipeSharingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/registerUser")
    public ResponseEntity<RecipeCreator> registerUser(@Valid @RequestBody UserDTO userDto){
        RecipeCreator existingUser = userService.findByUserName(userDto.getUserName());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            throw new EntityAlreadyExistException(RecipeCreator.class, "userName", userDto.getUserName());
        }

        RecipeCreator recipeUser = userService.saveUser(userDto);
        return new ResponseEntity<>(recipeUser, HttpStatus.CREATED);
    }

    @PostMapping("/createRecipe")
    public ResponseEntity<Object> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO,
                               @CurrentSecurityContext(expression="authentication?.name")
                               String userName){

        RecipeCreator existingUser = userService.findByUserName(userName);
        Recipe savedRecipe = recipeService.saveRecipe(recipeDTO,existingUser);

        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    @GetMapping("/searchRecipeByRecipeTitle/{recipeTitle}")
    public ResponseEntity<Object> searchRecipeByRecipeTitle(@Valid @PathVariable String recipeTitle){

        List<Recipe> foundRecipe = recipeService.searchRecipeByRecipeTitle(recipeTitle);
        if(foundRecipe != null && !foundRecipe.isEmpty() ) {
            return new ResponseEntity<Object>(foundRecipe, HttpStatus.FOUND);
        }
        return new ResponseEntity<Object>(foundRecipe, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/searchRecipeByUserName/{userNameToSearchAgainst}")
    public ResponseEntity<Object> searchRecipeByUserName(@Valid @PathVariable String userNameToSearchAgainst){

        List<Recipe> foundRecipe = recipeService.searchRecipeByUserName(userNameToSearchAgainst);
        List<RecipeSearchResult> recipeSearchResultList = new ArrayList<>();
        for(Recipe recipe : foundRecipe){
            RecipeSearchResult recipeSearchResult = new RecipeSearchResult(recipe.getTitle(),
                    recipe.getDescription(), recipe.getIngredients(),
                    recipe.getInstructions(), recipe.getServings(), recipe.getUser().getUserName());
            recipeSearchResultList.add(recipeSearchResult);
        }
        if(!recipeSearchResultList.isEmpty()) {
            return new ResponseEntity<Object>(recipeSearchResultList, HttpStatus.FOUND);
        }else {
            return new ResponseEntity<Object>(recipeSearchResultList, HttpStatus.NOT_FOUND);
        }
    }

}
