package recipes.service;

import org.springframework.http.ResponseEntity;
import recipes.dto.RecipeDto;
import recipes.dto.RecipeIdDto;
import recipes.dto.UserCredentialsDto;

import java.security.Principal;

public interface IRecipeService {
    ResponseEntity<?> registerNewUser(UserCredentialsDto userCredentialsDto);

    ResponseEntity<RecipeIdDto> addRecipe(RecipeDto recipeDto, Principal principal);

    ResponseEntity<RecipeDto> getRecipe(Long id);

    ResponseEntity<?> deleteRecipe(Long id, Principal principal);

    ResponseEntity<?> updateRecipe(Long id, RecipeDto recipeDto,Principal principal);

    ResponseEntity<?> findRecipe(String category, String name);
}
