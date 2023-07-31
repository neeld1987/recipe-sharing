package com.example.recipesharing.service;

import com.example.recipesharing.dto.UserDTO;
import com.example.recipesharing.model.RecipeCreator;
import com.example.recipesharing.respository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RecipeCreator saveUser(UserDTO userDto) {
        /*
        Save user in database again
         */
        RecipeCreator user = new RecipeCreator();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        RecipeCreator recipeUser = userRepository.save(user);
        return recipeUser;
    }

    @Override
    public RecipeCreator findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public RecipeCreator findByUserName(String userName) {
        RecipeCreator recipeUser = userRepository.findByUserName(userName);
        /*
        if(recipeUser == null){
            throw new EntityNotFoundException(RecipeUser.class, "userName", userName);
        }

         */
        return recipeUser;
    }

    private UserDTO mapToUserDto(RecipeCreator user){
        UserDTO userDto = new UserDTO();
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
