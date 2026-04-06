package j2ee.BuiTanThanh.repositories;

import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.itemInvoices ii LEFT JOIN FETCH ii.book b LEFT JOIN FETCH b.category LEFT JOIN FETCH i.user WHERE i.user = ?1 ORDER BY i.invoiceDate DESC")
    List<Invoice> findByUserOrderByInvoiceDateDesc(User user);

    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.itemInvoices ii LEFT JOIN FETCH ii.book b LEFT JOIN FETCH b.category LEFT JOIN FETCH i.user WHERE i.user.username = ?1 ORDER BY i.invoiceDate DESC")
    List<Invoice> findByUsernameOrderByInvoiceDateDesc(String username);

    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.itemInvoices ii LEFT JOIN FETCH ii.book b LEFT JOIN FETCH b.category LEFT JOIN FETCH i.user WHERE i.id = ?1")
    Optional<Invoice> findByIdWithDetails(Long id);
}
