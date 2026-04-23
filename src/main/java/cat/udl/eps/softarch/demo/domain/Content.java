package cat.udl.eps.softarch.demo.domain;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import java.util.List;



@Entity
@Data
//@EqualsAndHashCode(callSuper = true)
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    /*@ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @Column(nullable = false)
    private Project project;*/

    @ManyToMany
    @JoinTable(
        name = "content_tags",
        joinColumns = @JoinColumn(name = "content_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @NotBlank(message = "Name cannot be empty")
    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String body;

    @org.hibernate.validator.constraints.Length(max = 100)
    @Column(length = 100)
    private String description;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false) private Visibility visibility;
}