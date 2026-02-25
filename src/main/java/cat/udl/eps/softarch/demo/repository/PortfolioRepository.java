package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PortfolioRepository extends CrudRepository<Portfolio, Long>,
    PagingAndSortingRepository<Portfolio, Long> {

    List<Portfolio> findByOwner(@Param("owner") User owner);

    List<Portfolio> findByOwnerId(@Param("id") String id);

    List<Portfolio> findByVisibility(@Param("visibility") Visibility visibility);

    Optional<Portfolio> findByName(@Param("name") String name);

    List<Portfolio> findByNameContaining(@Param("text") String text);

    List<Portfolio> findByOwnerAndVisibility(@Param("owner") User owner,
                                             @Param("visibility") Visibility visibility);
}