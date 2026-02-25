package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private ZonedDateTime created;

    private ZonedDateTime modified;

    //@ManyToMany(mappedBy = "tags")
    //private Set<Content> contentSet = new HashSet<>();
}
