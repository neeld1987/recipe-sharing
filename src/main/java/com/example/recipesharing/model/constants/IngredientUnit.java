package com.example.recipesharing.model.constants;

import java.util.EnumSet;
import java.util.Set;

public enum IngredientUnit {

    g("Gram"), kg("Kilogram"), ml("Milliliter"), l("Liter"), pc("Piece"),
    tsp("Teaspoon"), tbsp("Tablespoon"), pinch("A dash");

    public static Set<IngredientUnit> allUnit = EnumSet.allOf(IngredientUnit.class);

    private String comment;

    private IngredientUnit(String comment) {
        this.comment = comment;
    }

    public String getComment() {

        return comment;
    }
}
