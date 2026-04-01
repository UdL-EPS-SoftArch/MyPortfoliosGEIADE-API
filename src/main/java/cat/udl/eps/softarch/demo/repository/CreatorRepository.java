package cat.udl.eps.softarch.demo.repository;

import java.util.List;
import cat.udl.eps.softarch.demo.domain.Creator;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface CreatorRepository extends CrudRepository< Creator, String>, PagingAndSortingRepository<Creator, String> {
    long countById(String id);

    long countByEmail(String email);
}