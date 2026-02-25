package cat.udl.eps.softarch.demo.domain;

import java.time.ZonedDateTime;

//import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
import lombok.Data;
//import lombok.EqualsAndHashCode;
//import jakarta.persistence.ManyToOne;
//import com.fasterxml.jackson.annotation.JsonIdentityReference;


@Entity
@Data
//@EqualsAndHashCode(callSuper = true)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    /*@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;*/

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @org.hibernate.validator.constraints.Length(max = 100)
    @Column(length = 100)
    private String reason;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}