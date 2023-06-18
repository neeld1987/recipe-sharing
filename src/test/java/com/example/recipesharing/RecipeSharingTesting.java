package com.example.recipesharing;

import helper.FileConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

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

        RequestBuilder searchRecipeByUserName = MockMvcRequestBuilders.get("/searchRecipeByRecipeTitle/user8").
                with(SecurityMockMvcRequestPostProcessors.user("user8").password("password").roles("USER"))
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
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Just make Spaghetti Carbonara"));

        mockMvc
                .perform(searchRecipeByUserName)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Just make Spaghetti Carbonara"));
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
                        value("RecipeUser already registered for {userName=user50}"));
    }

    @Test
    @DisplayName("Check if recipe search by title is working as expected")
    void createAndSearchRecipeByTile() throws Exception {

        RequestBuilder registerUser = MockMvcRequestBuilders.post("/registerUser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("register_valid_user_20.json"));

        RequestBuilder createRecipe = MockMvcRequestBuilders.post("/createRecipe").
                with(SecurityMockMvcRequestPostProcessors.user("user20").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileConverter.fromFile("recipe_valid_1.json"));

        RequestBuilder searchRecipeByTitle = MockMvcRequestBuilders.get("/searchRecipeByRecipeTitle/Spaghetti").
                with(SecurityMockMvcRequestPostProcessors.user("user20").password("password").roles("USER"))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerUser)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("user20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user20@gmail.com"));

        mockMvc
                .perform(createRecipe)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Just make Spaghetti Carbonara"));

        mockMvc
                .perform(searchRecipeByTitle)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title").value("Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description").value("Just make Spaghetti Carbonara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].value",
                containsInAnyOrder(8,500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].unit",
                        containsInAnyOrder("pc","g")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].ingredients.[*].type",
                        containsInAnyOrder("egg","spaghetti")));




    }




}
