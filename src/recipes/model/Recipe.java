package recipes.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String category;
    @NotNull
    @NotEmpty
    @Size(min = 8)
    private String date;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    @Column(name = "ingredient")
    private List<String> ingredients;

    @NotNull
    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    @Column(name = "directions")
    private List<String> directions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
