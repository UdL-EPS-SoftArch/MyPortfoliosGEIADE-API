package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ReportRepository extends CrudRepository<cat.udl.eps.softarch.demo.domain.Record, Long>, PagingAndSortingRepository<Record, Long> {
    // Buscar reportes por usuario
    List<Report> findByUserId(Long userId);

    // Buscar reportes por contenido reportado
    List<Report> findByContentId(Long contentId);
}