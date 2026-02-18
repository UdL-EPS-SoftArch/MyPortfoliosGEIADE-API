package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Entity
public class Project extends UriEntity<Long> {

    @Id
    private Long id;

    private String name;

    private String description;

    private ZonedDateTime created;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private ZonedDateTime modified;

    // Relació amb Portfolio (segons el diagrama, un Project pertany a un Portfolio)
    //@ManyToOne
    //private Portfolio portfolio;

    // Constructors, Getters i Setters
    public Project() {
        this.created = ZonedDateTime.now();
    }

    @Nullable
    @Override
    public Long getId() {
        return this.id;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

}