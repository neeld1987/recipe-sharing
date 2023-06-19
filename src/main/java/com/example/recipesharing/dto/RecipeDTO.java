package com.example.recipesharing.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    @NotEmpty
    private String title;

    private String description;

    private List<IngredientDTO> ingredients = new ArrayList<>();

    @NotEmpty
    private String instructions;

    private int servings;

    private UserDTO user;
}
