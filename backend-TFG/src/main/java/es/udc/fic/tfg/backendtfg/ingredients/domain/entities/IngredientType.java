package es.udc.fic.tfg.backendtfg.ingredients.domain.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ingredients", name = "ingredienttype")
public class IngredientType {
    @Id
    @GeneratedValue(generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @NotBlank
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
}
