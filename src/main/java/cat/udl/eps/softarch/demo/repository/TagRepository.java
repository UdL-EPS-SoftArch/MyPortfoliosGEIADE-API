package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Tag;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    boolean existsById(Long id);
    boolean existsByName(String name);
    Optional<Tag> findByName(String name);
    Optional<Tag> findById(Long id);
}
