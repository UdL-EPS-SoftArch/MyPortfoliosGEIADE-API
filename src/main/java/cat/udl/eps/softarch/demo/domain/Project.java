package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private ZonedDateTime created;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private ZonedDateTime modified;

    // Relació amb Portfolio (segons el diagrama, un Project pertany a un Portfolio)
    //@ManyToOne
    //private Portfolio portfolio;

    @Nullable
    @Override
    public Long getId() {
        return this.id;
    }
}