package j2ee.BuiTanThanh.services;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.entities.Category;
import j2ee.BuiTanThanh.repositories.IBookRepository;
import j2ee.BuiTanThanh.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
public class BookService {
    private final IBookRepository bookRepository;
    private final ICategoryRepository categoryRepository;

    public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }

    public List<Book> getAllBooksForAdmin() {
        return bookRepository.findAllBooksWithCategory();
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findActiveByIdWithCategory(id);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(@jakarta.validation.constraints.NotNull Book book) {
        Book existingBook = bookRepository.findById(book.getId()).orElse(null);
        Objects.requireNonNull(existingBook).setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        existingBook.setDescription(book.getDescription());
        if (book.getImage() != null && !book.getImage().isEmpty()) {
            existingBook.setImage(book.getImage());
        }
        bookRepository.save(existingBook);
    }

    public void deleteBookById(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setActive(false);
            bookRepository.save(book);
        });
    }

    public void activateBookById(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setActive(true);
            bookRepository.save(book);
        });
    }

    public List<Book> searchActiveBook(String keyword) {
        return bookRepository.searchActiveBook(keyword);
    }

    public List<Book> getActiveBooksByCategory(Long categoryId) {
        return bookRepository.findActiveBooksByCategory(categoryId);
    }

    public List<Book> searchBook(String keyword) {
        return bookRepository.searchBook(keyword);
    }

    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findBooksByCategory(categoryId);
    }

    public List<Book> getBooksWithFilters(Long categoryId, String keyword) {
        return bookRepository.findActiveBooksWithFilters(categoryId, keyword);
    }

    // ===== Excel Export =====
    public byte[] exportBooksToExcel() throws IOException {
        List<Book> books = bookRepository.findAllBooksWithCategory();
        try (XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Books");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Header row
            String[] headers = { "ID", "Title", "Author", "Price", "Category", "Description" };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getId() != null ? book.getId() : 0);
                row.createCell(1).setCellValue(book.getTitle() != null ? book.getTitle() : "");
                row.createCell(2).setCellValue(book.getAuthor() != null ? book.getAuthor() : "");
                row.createCell(3).setCellValue(book.getPrice() != null ? book.getPrice() : 0.0);
                row.createCell(4).setCellValue(book.getCategory() != null ? book.getCategory().getName() : "");
                row.createCell(5).setCellValue(book.getDescription() != null ? book.getDescription() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // ===== Excel Import =====
    public ImportResult importBooksFromExcel(MultipartFile file) throws IOException {
        int imported = 0, skipped = 0;
        List<String> errors = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                try {
                    String title = getCellString(row.getCell(0));
                    String author = getCellString(row.getCell(1));
                    String priceStr = getCellString(row.getCell(2));
                    String categoryName = getCellString(row.getCell(3));
                    String description = getCellString(row.getCell(4));

                    if (title.isBlank() || author.isBlank()) {
                        errors.add("Row " + (i + 1) + ": Title and Author are required");
                        skipped++;
                        continue;
                    }

                    double price = 0.0;
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        errors.add("Row " + (i + 1) + ": Invalid price '" + priceStr + "', set to 0.0");
                    }

                    // Find or create category
                    Category category = null;
                    if (!categoryName.isBlank()) {
                        category = categoryRepository.findByNameIgnoreCase(categoryName)
                                .orElseGet(() -> {
                                    Category newCat = Category.builder().name(categoryName).build();
                                    return categoryRepository.save(newCat);
                                });
                    }

                    Book book = Book.builder()
                            .title(title)
                            .author(author)
                            .price(price)
                            .category(category)
                            .description(description)
                            .active(true)
                            .build();
                    bookRepository.save(book);
                    imported++;
                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    skipped++;
                }
            }
        }
        return new ImportResult(imported, skipped, errors);
    }

    private String getCellString(Cell cell) {
        if (cell == null)
            return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val))
                    yield String.valueOf((long) val);
                else
                    yield String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public static record ImportResult(int imported, int skipped, List<String> errors) {
    }
}