package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.entities.Category;
import j2ee.BuiTanThanh.services.BookService;
import j2ee.BuiTanThanh.services.CategoryService;
import j2ee.BuiTanThanh.services.InvoiceService;
import j2ee.BuiTanThanh.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final InvoiceService invoiceService;
    private final UserService userService;

    @Value("${file.upload-dir:uploads/images}")
    private String uploadDir;

    // ===== DASHBOARD =====
    @GetMapping({ "", "/" })
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookService.countBooks());
        model.addAttribute("totalCategories", categoryService.getAllCategories().size());
        model.addAttribute("totalOrders", invoiceService.countInvoices());
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("recentBooks", bookService.getAllBooksForAdmin().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ===== BOOKS MANAGEMENT =====
    @GetMapping("/books")
    public String books(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category) {
        java.util.List<Book> books;
        if (keyword != null && !keyword.isBlank()) {
            books = bookService.searchBook(keyword.trim());
        } else if (category != null) {
            books = bookService.getBooksByCategory(category);
        } else {
            books = bookService.getAllBooksForAdmin();
        }
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("newBook", new Book());
        return "admin/books";
    }

    @PostMapping("/books/add")
    public String addBook(
            @ModelAttribute("newBook") Book book,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                book.setImage(saveImage(imageFile));
            }
            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("success", "Book \"" + book.getTitle() + "\" added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/books/edit")
    public String editBook(
            @ModelAttribute Book book,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                book.setImage(saveImage(imageFile));
            }
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBookById(id);
            redirectAttributes.addFlashAttribute("success", "Book deactivated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot deactivate book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/books/activate/{id}")
    public String activateBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.activateBookById(id);
            redirectAttributes.addFlashAttribute("success", "Book activated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot activate book: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    // ===== EXCEL EXPORT =====
    @GetMapping("/books/export")
    public ResponseEntity<byte[]> exportBooks() {
        try {
            byte[] data = bookService.exportBooksToExcel();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books_export.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===== EXCEL IMPORT TEMPLATE =====
    @GetMapping("/books/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Books");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] cols = { "Title", "Author", "Price", "Category", "Description" };
            for (int i = 0; i < cols.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                org.apache.poi.ss.usermodel.CellStyle style = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
                sheet.autoSizeColumn(i);
            }
            // Example row
            org.apache.poi.ss.usermodel.Row example = sheet.createRow(1);
            example.createCell(0).setCellValue("Clean Code");
            example.createCell(1).setCellValue("Robert C. Martin");
            example.createCell(2).setCellValue(39.99);
            example.createCell(3).setCellValue("Programming");
            example.createCell(4).setCellValue("A handbook of agile software craftsmanship");
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=import_template.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===== EXCEL IMPORT =====
    @PostMapping("/books/import")
    public String importBooks(@RequestParam("excelFile") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select an Excel file to import.");
            return "redirect:/admin/books";
        }
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            redirectAttributes.addFlashAttribute("error", "Only .xlsx or .xls files are supported.");
            return "redirect:/admin/books";
        }
        try {
            BookService.ImportResult result = bookService.importBooksFromExcel(file);
            String msg = String.format("Import complete: %d books added, %d skipped.", result.imported(),
                    result.skipped());
            if (!result.errors().isEmpty()) {
                msg += " Errors: " + String.join("; ", result.errors().stream().limit(3).toList());
            }
            redirectAttributes.addFlashAttribute("success", msg);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to read file: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    // ===== CATEGORIES MANAGEMENT =====
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategoriesWithBooks());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            if (name == null || name.isBlank()) {
                redirectAttributes.addFlashAttribute("error", "Category name is required.");
            } else {
                Category cat = Category.builder().name(name.trim()).build();
                categoryService.addCategory(cat);
                redirectAttributes.addFlashAttribute("success", "Category \"" + name + "\" added!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/edit")
    public String editCategory(@RequestParam Long id, @RequestParam String name,
            RedirectAttributes redirectAttributes) {
        try {
            Category cat = Category.builder().id(id).name(name.trim()).build();
            categoryService.updateCategory(cat);
            redirectAttributes.addFlashAttribute("success", "Category updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategoryById(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete: category may have associated books.");
        }
        return "redirect:/admin/categories";
    }

    // ===== HELPERS =====
    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : ".jpg";
        String filename = UUID.randomUUID() + extension;
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        return filename;
    }
}
