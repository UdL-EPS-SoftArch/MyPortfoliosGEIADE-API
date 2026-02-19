package cat.udl.eps.softarch.demo.domain;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Report extends Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @Column(nullable = false)
    private User user;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @Column(nullable = false)
    private Content content;

    @org.hibernate.validator.constraints.Length(max = 100)
    @Column(length = 100)
    private String reason;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}