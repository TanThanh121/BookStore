package j2ee.BuiTanThanh.repositories;

import j2ee.BuiTanThanh.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
        Optional<Category> findByNameIgnoreCase(String name);

        @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.books")
        List<Category> findAllWithBooks();
}