package cat.udl.eps.softarch.demo.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.domain.Profile;

import java.util.List;

@RepositoryRestResource
public interface ProfileRepository extends CrudRepository< Profile, Long>, PagingAndSortingRepository<Profile, Long> {
    List<Profile> findByCreator(@Param("creator") Creator creator);

}