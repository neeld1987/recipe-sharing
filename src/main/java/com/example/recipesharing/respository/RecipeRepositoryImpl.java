package com.example.recipesharing.respository;

import com.example.recipesharing.dto.RecipeSearchResult;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.RecipeCreator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeRepositoryImpl implements RecipeRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Recipe> searchRecipeByUserName(String userName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

        Join<Recipe, RecipeCreator> recipeUser = recipeRoot.join("user", JoinType.INNER);
        ParameterExpression<String> userNameParam = criteriaBuilder.parameter(String.class);
        criteriaQuery.where(criteriaBuilder.like(recipeUser.get("userName"), userNameParam));

        TypedQuery<Recipe> query = entityManager.createQuery(criteriaQuery);
        query.setParameter(userNameParam, "%" +userName +"%");

        List<Recipe> recipeList = query.getResultList();

        RecipeSearchResult recipeSearchResult = new RecipeSearchResult(recipeList.get(0).getTitle(),
                recipeList.get(0).getDescription(), recipeList.get(0).getIngredients(),
                recipeList.get(0).getInstructions(), recipeList.get(0).getServings(), recipeList.get(0).getUser().getUserName());


        return recipeList;
    }

    @Override
    public List<Recipe> searchRecipeByRecipeTitle(String recipeTitle) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

        criteriaQuery.select(recipeRoot);
        ParameterExpression<String> recipeTitleParam = criteriaBuilder.parameter(String.class);
        criteriaQuery.where(criteriaBuilder.like(recipeRoot.get("title"), recipeTitleParam));

        TypedQuery<Recipe> query = entityManager.createQuery(criteriaQuery);
        query.setParameter(recipeTitleParam, "%" +recipeTitle +"%");

        return query.getResultList();
    }
}
