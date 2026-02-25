package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Content;
//import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;


//import java.util.List;

@RepositoryRestResource
public interface ContentRepository extends CrudRepository<cat.udl.eps.softarch.demo.domain.Content, Long>, PagingAndSortingRepository<Content, Long> {
    Optional<Content> findByName(String name);

    boolean existsByName(String name);

    

    //List<Content> findByProjectId(Long projectId);

    //List<Content> findByProjectIdAndVisibility(Long projectId, Visibility visibility);
}