package recipes.service;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.dto.RecipeDto;
import recipes.dto.RecipeIdDto;
import recipes.dto.UserCredentialsDto;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repo.RecipeRepository;
import recipes.repo.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements IRecipeService {
    final
    RecipeRepository recipeRepository;
    UserRepository userRepository;
    final
    ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RecipeServiceImpl(RecipeRepository repo, UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.recipeRepository = repo;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> registerNewUser(UserCredentialsDto userCredentialsDto) {
        System.out.println(userCredentialsDto);
        if (userRepository.existsById(userCredentialsDto.getEmail())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = new User();
        user.setEmail(userCredentialsDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));

        userRepository.save(user);

        return ResponseEntity.status(200).build();
    }


    @Override
    @Transactional
    public ResponseEntity<RecipeIdDto> addRecipe(RecipeDto recipeDto, Principal principal) {
        recipeDto.setDate(String.valueOf(LocalDateTime.now()));
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        recipe.setDirections(recipeDto.getDirections());
        String userEmail = principal.getName();
        User user = userRepository.getById(userEmail);
        List<Recipe> recipesList = user.getRecipes().equals(null) ? new ArrayList<>() : user.getRecipes();
        recipe.setUser(user);
        recipesList.add(recipeRepository.save(recipe));

        return ResponseEntity.ok(modelMapper.map(recipe, RecipeIdDto.class));
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipe(Long id) {

        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Recipe recipe = recipeRepository.getById(id);
        return ResponseEntity.ok(modelMapper.map(recipe, RecipeDto.class));
    }

    @Override
    public ResponseEntity<?> deleteRecipe(Long id, Principal principal) {

        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            Recipe recipe = recipeRepository.getById(id);
            User author = recipe.getUser();
            if (author.getEmail().equals(principal.getName())) {
                recipeRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateRecipe(Long id, RecipeDto recipeDto, Principal principal) {

        if (recipeRepository.existsById(id)) {
            Recipe existingRecipe = recipeRepository.getById(id);

            User author = existingRecipe.getUser();

            if (author.getEmail().equals(principal.getName())) {
                modelMapper.map(recipeDto, existingRecipe);
                existingRecipe.setDate(LocalDateTime.now().toString());
                existingRecipe.setDirections(recipeDto.getDirections());

                recipeRepository.save(existingRecipe);

                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        }
        return ResponseEntity.notFound().build();

    }

    @Override
    public ResponseEntity<?> findRecipe(String category, String name) {

        if (StringUtils.isBlank(category) == StringUtils.isBlank(name)) {
            return ResponseEntity.badRequest().build();
        }

        List<Recipe> recipes;

        if (StringUtils.isNotBlank(category)) {
            recipes = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        } else {
            recipes = recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
        }

        List<RecipeDto> recipeDtos = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipeDtos);
    }


}
