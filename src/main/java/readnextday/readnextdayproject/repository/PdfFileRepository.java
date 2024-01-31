package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.PdfFile;

public interface PdfFileRepository extends JpaRepository<PdfFile, Long> {
}
