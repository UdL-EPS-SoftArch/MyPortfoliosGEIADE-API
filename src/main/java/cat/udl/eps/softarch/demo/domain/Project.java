package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Portfolio portfolio;

    public Project() {
        this.created = ZonedDateTime.now();
    }

    @Nullable
    @Override
    public Long getId() {
        return this.id;
    }

    public ZonedDateTime getCreated() { return this.created;}

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

}
