package j2ee.BuiTanThanh.services;

import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.entities.ItemInvoice;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.repositories.IInvoiceRepository;
import j2ee.BuiTanThanh.repositories.IItemInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
public class InvoiceService {

    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;

    public List<Invoice> getInvoicesByUser(User user) {
        return invoiceRepository.findByUserOrderByInvoiceDateDesc(user);
    }

    public List<Invoice> getInvoicesByUsername(String username) {
        return invoiceRepository.findByUsernameOrderByInvoiceDateDesc(username);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findByIdWithDetails(id);
    }

    public Invoice createInvoice(User user, Double totalPrice) {
        Invoice invoice = Invoice.builder()
                .user(user)
                .price(totalPrice)
                .invoiceDate(new Date())
                .build();
        return invoiceRepository.save(invoice);
    }

    public void addItemToInvoice(Invoice invoice, ItemInvoice itemInvoice) {
        itemInvoice.setInvoice(invoice);
        itemInvoiceRepository.save(itemInvoice);
    }

    public Double calculateInvoiceTotal(Invoice invoice) {
        return invoice.getItemInvoices().stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }

    public long countInvoices() {
        return invoiceRepository.count();
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}