package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Content;
//import cat.udl.eps.softarch.demo.domain.Visibility;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

import java.util.List;


//import java.util.List;

@RepositoryRestResource
public interface ContentRepository extends CrudRepository<cat.udl.eps.softarch.demo.domain.Content, Long>, PagingAndSortingRepository<Content, Long> {
    Optional<Content> findByName(String name);

    boolean existsByName(String name);

    List<Content> findAll();
    List<Content> findByTags_Id(Long tagId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = "tags")
    @Query("select c from Content c where c.contentId = :contentId")
    Optional<Content> findByContentIdWithTagsForUpdate(@Param("contentId") Long contentId);

    @Query("""
            select distinct c from Content c
            where c.contentId not in (
                select tagged.contentId from Content tagged join tagged.tags t
                where t.id = :tagId
            )
            """)
    List<Content> findAvailableForTagId(@Param("tagId") Long tagId);

    //List<Content> findByProjectId(Long projectId);

    //List<Content> findByProjectIdAndVisibility(Long projectId, Visibility visibility);
}
