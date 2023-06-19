package com.example.recipesharing.respository;

import com.example.recipesharing.model.RecipeCreator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RecipeCreator, Long> {

    RecipeCreator findByEmail(String email);

    RecipeCreator findByUserName(String userName);


}


