package com.example.recipesharing.dto;

import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.constants.IngredientUnit;
import com.example.recipesharing.model.converter.IngredientUnitConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    @NotNull
    private int value;

    @NotNull
    @Convert(converter= IngredientUnitConverter.class)
    private IngredientUnit unit;

    @NotEmpty
    private String type;

    private Recipe recipe;
}
