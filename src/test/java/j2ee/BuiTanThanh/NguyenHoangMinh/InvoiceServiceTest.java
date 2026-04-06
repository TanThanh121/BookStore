package j2ee.BuiTanThanh.NguyenHoangMinh;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.entities.ItemInvoice;
import j2ee.BuiTanThanh.repositories.IInvoiceRepository;
import j2ee.BuiTanThanh.repositories.IItemInvoiceRepository;
import j2ee.BuiTanThanh.services.InvoiceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * White Box Tests - Nguyễn Hoàng Minh
 * Phạm vi: InvoiceService (TC_NHM_011 → TC_NHM_016)
 * Công cụ: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private IInvoiceRepository invoiceRepository;

    @Mock
    private IItemInvoiceRepository itemInvoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    // =========================================================
    // TC_NHM_011: getInvoicesByUsername trả về danh sách đơn hàng của user
    // Luồng code: invoiceRepository.findByUsernameOrderByInvoiceDateDesc(username)
    // =========================================================
    @Test
    @DisplayName("TC_NHM_011 - getInvoicesByUsername trả về danh sách đơn hàng của user")
    void getInvoicesByUsername_shouldReturnUserInvoices() {
        Invoice inv1 = Invoice.builder().id(1L).build();
        Invoice inv2 = Invoice.builder().id(2L).build();
        Invoice inv3 = Invoice.builder().id(3L).build();
        when(invoiceRepository.findByUsernameOrderByInvoiceDateDesc("alice"))
                .thenReturn(Arrays.asList(inv1, inv2, inv3));

        List<Invoice> result = invoiceService.getInvoicesByUsername("alice");

        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(inv1, inv2, inv3);
    }

    // =========================================================
    // TC_NHM_012: getInvoicesByUsername trả về danh sách rỗng khi user chưa có đơn
    // hàng
    // Luồng code: invoiceRepository.findByUsernameOrderByInvoiceDateDesc() → empty
    // list
    // =========================================================
    @Test
    @DisplayName("TC_NHM_012 - getInvoicesByUsername trả về danh sách rỗng khi user chưa đặt hàng")
    void getInvoicesByUsername_shouldReturnEmpty_whenUserHasNoOrders() {
        when(invoiceRepository.findByUsernameOrderByInvoiceDateDesc("newuser"))
                .thenReturn(Collections.emptyList());

        List<Invoice> result = invoiceService.getInvoicesByUsername("newuser");

        assertThat(result).isEmpty();
    }

    // =========================================================
    // TC_NHM_013: getInvoiceById trả về Optional<Invoice> khi tìm thấy
    // Luồng code: invoiceRepository.findByIdWithDetails(id) — nhánh tìm thấy
    // =========================================================
    @Test
    @DisplayName("TC_NHM_013 - getInvoiceById trả về Optional<Invoice> khi tìm thấy")
    void getInvoiceById_shouldReturnInvoice_whenFound() {
        Invoice invoice = Invoice.builder().id(10L).build();
        when(invoiceRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(invoice));

        Optional<Invoice> result = invoiceService.getInvoiceById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(10L);
    }

    // =========================================================
    // TC_NHM_014: getInvoiceById trả về Optional.empty() khi không tìm thấy
    // Luồng code: invoiceRepository.findByIdWithDetails(id) — nhánh không tìm thấy
    // =========================================================
    @Test
    @DisplayName("TC_NHM_014 - getInvoiceById trả về Optional.empty() khi không tìm thấy")
    void getInvoiceById_shouldReturnEmpty_whenNotFound() {
        when(invoiceRepository.findByIdWithDetails(9999L)).thenReturn(Optional.empty());

        Optional<Invoice> result = invoiceService.getInvoiceById(9999L);

        assertThat(result).isEmpty();
    }

    // =========================================================
    // TC_NHM_015: calculateInvoiceTotal tính đúng tổng tiền
    // Luồng code: stream itemInvoices → book.price * quantity → sum
    // Kịch bản: item1 (100.000 × 2) + item2 (50.000 × 3) = 350.000
    // =========================================================
    @Test
    @DisplayName("TC_NHM_015 - calculateInvoiceTotal tính đúng tổng tiền từ các sản phẩm")
    void calculateInvoiceTotal_shouldReturnCorrectSum() {
        Book book1 = Book.builder().id(1L).price(100000.0).build();
        Book book2 = Book.builder().id(2L).price(50000.0).build();

        ItemInvoice item1 = ItemInvoice.builder().book(book1).quantity(2).build();
        ItemInvoice item2 = ItemInvoice.builder().book(book2).quantity(3).build();

        Invoice invoice = Invoice.builder()
                .id(1L)
                .itemInvoices(Arrays.asList(item1, item2))
                .build();

        double total = invoiceService.calculateInvoiceTotal(invoice);

        // 100.000 * 2 + 50.000 * 3 = 350.000
        assertThat(total).isEqualTo(350000.0);
    }

    // =========================================================
    // TC_NHM_016: countInvoices trả về đúng tổng số đơn hàng
    // Luồng code: invoiceRepository.count()
    // =========================================================
    @Test
    @DisplayName("TC_NHM_016 - countInvoices trả về đúng tổng số đơn hàng")
    void countInvoices_shouldReturnCorrectCount() {
        when(invoiceRepository.count()).thenReturn(42L);

        long count = invoiceService.countInvoices();

        assertThat(count).isEqualTo(42L);
        verify(invoiceRepository).count();
    }
}
