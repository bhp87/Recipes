package recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    @NotBlank
    @NotNull
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String category;
    private String date;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private List<String> ingredients;
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private List<String> directions;

}
