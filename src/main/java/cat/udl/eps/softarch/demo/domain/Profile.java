package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    private String description;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @OneToOne(mappedBy = "profile")
    private Creator creator;

    public Profile() {}
}