package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ReportRepository extends CrudRepository<cat.udl.eps.softarch.demo.domain.Report, Long>, PagingAndSortingRepository<Report, Long> {
    // Buscar reportes por usuario
    //List<Report> findByUserId(Long userId);

    // Buscar reportes por contenido reportado
    List<Report> findByContent_ContentId(Long contentId);
}