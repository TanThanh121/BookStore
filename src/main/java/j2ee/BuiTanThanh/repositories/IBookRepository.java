package j2ee.BuiTanThanh.repositories;

import j2ee.BuiTanThanh.entities.Book;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends
                PagingAndSortingRepository<Book, Long>, JpaRepository<Book, Long> {
        @Query("SELECT b FROM Book b JOIN FETCH b.category ORDER BY b.id")
        List<Book> findAllBooksWithCategory();

        @Query("SELECT b FROM Book b JOIN FETCH b.category WHERE b.active = true ORDER BY b.id")
        List<Book> findAllActiveBooksWithCategory();

        @Query("SELECT b FROM Book b JOIN FETCH b.category WHERE b.id = ?1")
        Optional<Book> findByIdWithCategory(Long id);

        @Query("SELECT b FROM Book b JOIN FETCH b.category WHERE b.id = ?1 AND b.active = true")
        Optional<Book> findActiveByIdWithCategory(Long id);

        long countByActiveTrue();

        default List<Book> findAllBooks(Integer pageNo,
                        Integer pageSize,
                        String sortBy) {
                return findAllActiveBooksWithCategory();
        }

        @Query("""
                        SELECT b FROM Book b JOIN FETCH b.category
                        WHERE b.title LIKE %?1%
                        OR b.author LIKE %?1%
                        OR b.category.name LIKE %?1%
                        """)
        List<Book> searchBook(String keyword);

        @Query("""
                        SELECT b FROM Book b JOIN FETCH b.category
                        WHERE b.active = true
                        AND (b.title LIKE %?1% OR b.author LIKE %?1% OR b.category.name LIKE %?1%)
                        """)
        List<Book> searchActiveBook(String keyword);

        @Query("SELECT b FROM Book b JOIN FETCH b.category WHERE b.category.id = ?1")
        List<Book> findBooksByCategory(Long categoryId);

        @Query("SELECT b FROM Book b JOIN FETCH b.category WHERE b.category.id = ?1 AND b.active = true")
        List<Book> findActiveBooksByCategory(Long categoryId);

        @Query("""
                        SELECT b FROM Book b JOIN FETCH b.category
                        WHERE (?1 IS NULL OR b.category.id = ?1)
                        AND (?2 IS NULL OR b.title LIKE %?2% OR b.author LIKE %?2% OR b.category.name LIKE %?2%)
                        """)
        List<Book> findBooksWithFilters(Long categoryId, String keyword);

        @Query("""
                        SELECT b FROM Book b JOIN FETCH b.category
                        WHERE b.active = true
                        AND (?1 IS NULL OR b.category.id = ?1)
                        AND (?2 IS NULL OR b.title LIKE %?2% OR b.author LIKE %?2% OR b.category.name LIKE %?2%)
                        """)
        List<Book> findActiveBooksWithFilters(Long categoryId, String keyword);
}