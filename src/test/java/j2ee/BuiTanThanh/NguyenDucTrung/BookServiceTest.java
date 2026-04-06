package j2ee.BuiTanThanh.NguyenDucTrung;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.repositories.IBookRepository;
import j2ee.BuiTanThanh.repositories.ICategoryRepository;
import j2ee.BuiTanThanh.services.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * White Box Tests - Nguyễn Đức Trung
 * Phạm vi: BookService (TC_NDT_001 → TC_NDT_012)
 * Công cụ: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    // =========================================================
    // TC_NDT_001: getAllBooks trả về danh sách từ repository
    // =========================================================
    @Test
    @DisplayName("TC_NDT_001 - getAllBooks trả về danh sách sách đúng")
    void getAllBooks_shouldReturnListFromRepository() {
        List<Book> mockBooks = List.of(
                Book.builder().id(1L).title("Book 1").author("A1").price(50.0).build(),
                Book.builder().id(2L).title("Book 2").author("A2").price(60.0).build(),
                Book.builder().id(3L).title("Book 3").author("A3").price(70.0).build(),
                Book.builder().id(4L).title("Book 4").author("A4").price(80.0).build(),
                Book.builder().id(5L).title("Book 5").author("A5").price(90.0).build());
        when(bookRepository.findAllBooks(0, 5, "id")).thenReturn(mockBooks);

        List<Book> result = bookService.getAllBooks(0, 5, "id");

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getTitle()).isEqualTo("Book 1");
        verify(bookRepository).findAllBooks(0, 5, "id");
    }

    // =========================================================
    // TC_NDT_002: getBookById trả về sách khi ID tồn tại và active
    // =========================================================
    @Test
    @DisplayName("TC_NDT_002 - getBookById trả về Optional<Book> khi ID tồn tại")
    void getBookById_shouldReturnBook_whenIdExists() {
        Book book = Book.builder().id(1L).title("Java Core").author("Author A").active(true).build();
        when(bookRepository.findActiveByIdWithCategory(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().isActive()).isTrue();
    }

    // =========================================================
    // TC_NDT_003: getBookById trả về Optional.empty() khi ID không tồn tại
    // =========================================================
    @Test
    @DisplayName("TC_NDT_003 - getBookById trả về Optional.empty() khi ID không tồn tại")
    void getBookById_shouldReturnEmpty_whenIdNotFound() {
        when(bookRepository.findActiveByIdWithCategory(9999L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(9999L);

        assertThat(result).isEmpty();
    }

    // =========================================================
    // TC_NDT_004: addBook gọi repository.save với đúng đối tượng
    // =========================================================
    @Test
    @DisplayName("TC_NDT_004 - addBook gọi repository.save để lưu sách mới")
    void addBook_shouldCallRepositorySave() {
        Book book = Book.builder()
                .title("New Book")
                .author("New Author")
                .price(99.0)
                .build();

        bookService.addBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    // =========================================================
    // TC_NDT_005: updateBook cập nhật title, author, price khi sách tồn tại
    // =========================================================
    @Test
    @DisplayName("TC_NDT_005 - updateBook cập nhật title, author, price đúng")
    void updateBook_shouldUpdateFields_whenBookExists() {
        Book existing = Book.builder()
                .id(1L).title("Old Title").author("Old Author").price(50.0).image("img.jpg")
                .build();
        Book update = Book.builder()
                .id(1L).title("Updated Title").author("Updated Author").price(150.0).image("img.jpg")
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));

        bookService.updateBook(update);

        assertThat(existing.getTitle()).isEqualTo("Updated Title");
        assertThat(existing.getAuthor()).isEqualTo("Updated Author");
        assertThat(existing.getPrice()).isEqualTo(150.0);
        verify(bookRepository).save(existing);
    }

    // =========================================================
    // TC_NDT_006: updateBook KHÔNG ghi đè ảnh cũ khi ảnh mới rỗng
    // Luồng code: if (book.getImage() != null && !book.getImage().isEmpty()) →
    // false
    // =========================================================
    @Test
    @DisplayName("TC_NDT_006 - updateBook giữ nguyên ảnh cũ khi image mới là chuỗi rỗng")
    void updateBook_shouldNotOverwriteImage_whenNewImageIsEmpty() {
        Book existing = Book.builder()
                .id(1L).title("Title").author("Author").price(50.0).image("old_image.jpg")
                .build();
        Book update = Book.builder()
                .id(1L).title("Title").author("Author").price(50.0).image("") // image rỗng
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));

        bookService.updateBook(update);

        // Nhánh false: image không bị ghi đè
        assertThat(existing.getImage()).isEqualTo("old_image.jpg");
    }

    // =========================================================
    // TC_NDT_007: deleteBookById đặt active=false (soft delete)
    // Luồng code: book.setActive(false) → bookRepository.save(book)
    // =========================================================
    @Test
    @DisplayName("TC_NDT_007 - deleteBookById thực hiện soft delete (active=false)")
    void deleteBookById_shouldSetActiveFalse_whenBookExists() {
        Book book = Book.builder().id(1L).title("Book").author("A").active(true).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1L);

        assertThat(book.isActive()).isFalse();
        verify(bookRepository).save(book);
    }

    // =========================================================
    // TC_NDT_008: activateBookById đặt lại active=true
    // Luồng code: book.setActive(true) → bookRepository.save(book)
    // =========================================================
    @Test
    @DisplayName("TC_NDT_008 - activateBookById đặt lại active=true cho sách đã bị vô hiệu hóa")
    void activateBookById_shouldSetActiveTrue_whenBookIsInactive() {
        Book book = Book.builder().id(1L).title("Book").author("A").active(false).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.activateBookById(1L);

        assertThat(book.isActive()).isTrue();
        verify(bookRepository).save(book);
    }

    // =========================================================
    // TC_NDT_009: searchActiveBook trả về đúng danh sách (chỉ active)
    // =========================================================
    @Test
    @DisplayName("TC_NDT_009 - searchActiveBook chỉ trả về sách active có từ khóa phù hợp")
    void searchActiveBook_shouldReturnOnlyActiveBooks() {
        Book active1 = Book.builder().id(1L).title("Java Core").active(true).build();
        Book active2 = Book.builder().id(2L).title("Java Advanced").active(true).build();
        when(bookRepository.searchActiveBook("Java")).thenReturn(List.of(active1, active2));

        List<Book> result = bookService.searchActiveBook("Java");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Book::isActive);
        assertThat(result).allMatch(b -> b.getTitle().contains("Java"));
    }

    // =========================================================
    // TC_NDT_010: getBooksWithFilters lọc đúng khi kết hợp keyword và category
    // =========================================================
    @Test
    @DisplayName("TC_NDT_010 - getBooksWithFilters trả về sách đúng category và đúng keyword")
    void getBooksWithFilters_shouldReturnFilteredBooks() {
        Book book = Book.builder().id(1L).title("Java Programming").author("Author").build();
        when(bookRepository.findActiveBooksWithFilters(1L, "Java")).thenReturn(List.of(book));

        List<Book> result = bookService.getBooksWithFilters(1L, "Java");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
        verify(bookRepository).findActiveBooksWithFilters(1L, "Java");
    }

    // =========================================================
    // TC_NDT_011: deleteBookById với ID không tồn tại không ném ngoại lệ
    // Luồng code: Optional.empty() → ifPresent không chạy → không có save
    // =========================================================
    @Test
    @DisplayName("TC_NDT_011 - deleteBookById không ném exception khi ID không tồn tại")
    void deleteBookById_shouldNotThrowException_whenIdNotFound() {
        when(bookRepository.findById(9999L)).thenReturn(Optional.empty());

        // Không ném ngoại lệ
        bookService.deleteBookById(9999L);

        // Lambda ifPresent không chạy → save không được gọi
        verify(bookRepository, never()).save(any());
    }

    // =========================================================
    // TC_NDT_012: countBooks trả về đúng số lượng
    // =========================================================
    @Test
    @DisplayName("TC_NDT_012 - countBooks trả về đúng tổng số sách")
    void countBooks_shouldReturnCorrectCount() {
        when(bookRepository.count()).thenReturn(10L);

        long count = bookService.countBooks();

        assertThat(count).isEqualTo(10L);
        verify(bookRepository).count();
    }
}
