package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Portfolio extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime modified;

    // Portfolio owned by 1 User
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now();
        this.created = now;
        this.modified = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.modified = ZonedDateTime.now();
    }
}