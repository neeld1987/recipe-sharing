package com.example.recipesharing.service;

import com.example.recipesharing.dto.UserDTO;
import com.example.recipesharing.model.RecipeCreator;

public interface UserService {

    RecipeCreator saveUser(UserDTO userDto);

    RecipeCreator findUserByEmail(String email);

    RecipeCreator findByUserName( String userName);
}
