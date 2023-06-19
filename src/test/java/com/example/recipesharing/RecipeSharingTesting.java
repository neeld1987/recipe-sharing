package com.example.recipesharing;

import helper.FileConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RecipeSharingTesting {

    @Autowired
    private MockMvc mockMvc;

    FileConverter fileConverter = new FileConverter();

    @Test
    @DisplayName("Check if spring security applies to the endpoint")
    void successIfSecurityApplies() throws Exception {
        // no auth for /registerUser
        RequestBuilder registerUser = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_1.json"));

        // basic auth for rest of the three endpoints
        RequestBuilder createRecipe = MockMvcRequestBuilders.post("/createRecipe").
                with(SecurityMockMvcRequestPostProcessors.user("user8").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_1.json"));

        RequestBuilder searchRecipeByTitle = MockMvcRequestBuilders.get("/searchRecipeByRecipeTitle/Spaghetti Carbonara").
                with(SecurityMockMvcRequestPostProcessors.user("user8").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        RequestBuilder searchRecipeByUserName = MockMvcRequestBuilders.get("/searchRecipeByUserName/user8").
                with(SecurityMockMvcRequestPostProcessors.user("user8").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // same three requests without basic auth - execution of these three should fail
        RequestBuilder createRecipeWithoutAuth = MockMvcRequestBuilders.post("/createRecipe")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_1.json"));

        RequestBuilder searchRecipeByTitleWithoutAuth = MockMvcRequestBuilders.get("/searchRecipeByRecipeTitle/Spaghetti Carbonara")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        RequestBuilder searchRecipeByUserNameWithoutAuth = MockMvcRequestBuilders.get("/searchRecipeByUserName/user8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerUser)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user8@gmail.com"));

        mockMvc
                .perform(createRecipe)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Just make Spaghetti Carbonara"));

        mockMvc
                .perform(searchRecipeByTitle)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description").value("Just make Spaghetti Carbonara"));

        mockMvc
                .perform(searchRecipeByUserName)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description").value("Just make Spaghetti Carbonara"));

        // next three requests should fail
        mockMvc
                .perform(createRecipeWithoutAuth)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        mockMvc
                .perform(searchRecipeByTitleWithoutAuth)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        mockMvc
                .perform(searchRecipeByUserNameWithoutAuth)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }


    @Test
    @DisplayName("Check if same user registration failing")
    void failIfRegisterSameUserTwice() throws Exception {

        RequestBuilder registerUser = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_50.json"));

        mockMvc.perform(registerUser)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user50"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user50@gmail.com"));

        mockMvc.perform(registerUser)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").
                        value("RecipeCreator already registered for {userName=user50}"));
    }

    @Test
    @DisplayName("Check if recipe search by title is working as expected")
    void createAndSearchRecipeByTitle() throws Exception {

        RequestBuilder registerUser = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_20.json"));

        RequestBuilder createRecipe = MockMvcRequestBuilders.post("/createRecipe").
                with(SecurityMockMvcRequestPostProcessors.user("user20").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_3.json"));

        RequestBuilder searchRecipeByTitle = MockMvcRequestBuilders.get("/searchRecipeByRecipeTitle/Rice").
                with(SecurityMockMvcRequestPostProcessors.user("user20").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerUser)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user20@gmail.com"));

        mockMvc
                .perform(createRecipe)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Fried Rice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Easy Fried Rice"));

        mockMvc
                .perform(searchRecipeByTitle)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title", contains("Fried Rice")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description").value("Easy Fried Rice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].value",
                containsInAnyOrder(4, 150,500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].unit",
                        containsInAnyOrder("pc","g","g")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].type",
                        containsInAnyOrder("egg","rice","chicken")));




    }

    @Test
    @DisplayName("Check if recipe search by username is working as expected")
    void createAndSearchRecipeByUserName() throws Exception {

        RequestBuilder registerUser_55 = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_55.json"));

        RequestBuilder registerUser_56 = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_56.json"));

        RequestBuilder createRecipe_by_user_55 = MockMvcRequestBuilders.post("/createRecipe").
                with(SecurityMockMvcRequestPostProcessors.user("user55").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_1.json"));

        RequestBuilder createRecipe_by_user_56 = MockMvcRequestBuilders.post("/createRecipe").
                with(SecurityMockMvcRequestPostProcessors.user("user56").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_2.json"));

        RequestBuilder searchRecipeByUserName_55 = MockMvcRequestBuilders.get("/searchRecipeByUserName/user55").
                with(SecurityMockMvcRequestPostProcessors.user("user55").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerUser_55)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user55"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user55@gmail.com"));

        mockMvc.perform(registerUser_56)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user56"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user56@gmail.com"));

        mockMvc
                .perform(createRecipe_by_user_55)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Just make Spaghetti Carbonara"));


        mockMvc
                .perform(createRecipe_by_user_56)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Eggplant shakshuka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Eggplant shakshuka made easy"));

        mockMvc
                .perform(searchRecipeByUserName_55)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].userName").value("user55"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description").value("Just make Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].value",
                        containsInAnyOrder(8,500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].unit",
                        containsInAnyOrder("pc","g")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].type",
                        containsInAnyOrder("egg","spaghetti")));

    }

}
