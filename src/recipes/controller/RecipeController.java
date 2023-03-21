package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDto;
import recipes.dto.UserCredentialsDto;
import recipes.service.IRecipeService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class RecipeController {
    final
    IRecipeService recipeService;


    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return recipeService.registerNewUser(userCredentialsDto);
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody RecipeDto recipeDto,Principal principal) {
        return recipeService.addRecipe(recipeDto,principal);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable Long id, Principal principal) {
        return recipeService.deleteRecipe(id, principal);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDto recipeDto, Principal principal) {
        return recipeService.updateRecipe(id, recipeDto, principal);
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<?> findRecipe(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        return recipeService.findRecipe(category, name);
    }


}
