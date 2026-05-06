package cat.udl.eps.softarch.demo.domain;

import java.time.ZonedDateTime;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

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

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports;

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
    @Column(nullable = false)
    private Visibility visibility;
}