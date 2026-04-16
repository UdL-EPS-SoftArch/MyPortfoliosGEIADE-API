package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProjectRepository extends CrudRepository<Project, Long>, PagingAndSortingRepository<Project, Long> {

    List<Project> findByNameContaining(@Param("name") String name);

    List<Project> findByPortfolio(@Param("portfolio") Portfolio portfolio);

    List<Project> findByPortfolioAndVisibility(@Param("portfolio") Portfolio portfolio,
                                               @Param("visibility") Visibility visibility);

    List<Project> findByVisibility(@Param("visibility") Visibility visibility);
}
