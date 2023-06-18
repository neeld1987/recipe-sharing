package com.example.recipesharing.model.converter;

import com.example.recipesharing.model.constants.IngredientUnit;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class IngredientUnitConverter implements AttributeConverter<IngredientUnit, String> {

    @Override
    public String convertToDatabaseColumn(IngredientUnit ingredientUnit) {
        if (ingredientUnit == null) {
            return null;
        }
        return ingredientUnit.getComment();
    }

    @Override
    public IngredientUnit convertToEntityAttribute(String comment) {
        if (comment == null) {
            return null;
        }

        return Stream.of(IngredientUnit.values())
                .filter(c -> c.getComment().equals(comment))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
