package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.daos.Item;
import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.services.BookService;
import j2ee.BuiTanThanh.services.CartService;
import j2ee.BuiTanThanh.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final CartService cartService;

        @Value("${file.upload-dir:uploads/images}")
        private String uploadDir;

        private static final int PAGE_SIZE = 20;

        @GetMapping
        public String showAllBooks(@NotNull Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "default") String sortBy,
                        @RequestParam(required = false) Long category,
                        @RequestParam(required = false) String keyword) {
                return loadBookList(model, pageNo, sortBy, category, keyword);
        }

        // /books/search cũng dùng chung logic
        @GetMapping("/search")
        public String searchBooks(@NotNull Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "default") String sortBy,
                        @RequestParam(required = false) Long category,
                        @RequestParam(required = false) String keyword) {
                return loadBookList(model, pageNo, sortBy, category, keyword);
        }

        private String loadBookList(Model model, int pageNo, String sortBy, Long category, String keyword) {
                // 1. Lấy toàn bộ danh sách phù hợp
                List<Book> allBooks;
                if (keyword != null && !keyword.trim().isEmpty() && category != null) {
                        allBooks = new java.util.ArrayList<>(bookService.getBooksWithFilters(category, keyword.trim()));
                } else if (keyword != null && !keyword.trim().isEmpty()) {
                        allBooks = new java.util.ArrayList<>(bookService.searchActiveBook(keyword.trim()));
                } else if (category != null) {
                        allBooks = new java.util.ArrayList<>(bookService.getActiveBooksByCategory(category));
                } else {
                        allBooks = new java.util.ArrayList<>(bookService.getAllBooks(0, Integer.MAX_VALUE, "id"));
                }

                // 2. Sắp xếp theo sortBy
                switch (sortBy) {
                        case "title_asc" ->
                                allBooks.sort(java.util.Comparator.comparing(b -> b.getTitle().toLowerCase()));
                        case "title_desc" -> allBooks.sort(java.util.Comparator
                                        .comparing((Book b) -> b.getTitle().toLowerCase()).reversed());
                        case "price_asc" -> allBooks.sort(java.util.Comparator.comparingDouble(Book::getPrice));
                        case "price_desc" ->
                                allBooks.sort(java.util.Comparator.comparingDouble(Book::getPrice).reversed());
                        default -> {
                        } // giữ nguyên thứ tự mặc định (theo id)
                }

                // 3. Tính phân trang
                int totalResults = allBooks.size();
                int totalPages = Math.max(1, (int) Math.ceil(totalResults / (double) PAGE_SIZE));
                pageNo = Math.max(0, Math.min(pageNo, totalPages - 1));

                int fromIndex = pageNo * PAGE_SIZE;
                int toIndex = Math.min(fromIndex + PAGE_SIZE, totalResults);
                List<Book> pageBooks = allBooks.subList(fromIndex, toIndex);

                model.addAttribute("books", pageBooks);
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("totalResults", totalResults);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("keyword", keyword);
                model.addAttribute("selectedCategory", category);
                model.addAttribute("sortBy", sortBy);
                return "book/list";
        }

        @GetMapping("/add")
        public String addBookForm(@NotNull Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(
                        @Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/add";
                }

                // Handle file upload
                if (!imageFile.isEmpty()) {
                        try {
                                String fileName = saveImage(imageFile);
                                book.setImage(fileName);
                        } catch (IOException e) {
                                model.addAttribute("error", "Failed to upload image: " + e.getMessage());
                                model.addAttribute("categories", categoryService.getAllCategories());
                                return "book/add";
                        }
                }

                bookService.addBook(book);
                return "redirect:/books";
        }

        @GetMapping("/edit/{id}")
        public String editBookForm(@NotNull Model model, @PathVariable long id) {
                var book = bookService.getBookById(id);
                model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/edit";
        }

        @PostMapping("/edit")
        public String editBook(@Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/edit";
                }

                // Handle file upload only if new file is provided
                if (!imageFile.isEmpty()) {
                        try {
                                String fileName = saveImage(imageFile);
                                book.setImage(fileName);
                        } catch (IOException e) {
                                model.addAttribute("error", "Failed to upload image: " + e.getMessage());
                                model.addAttribute("categories", categoryService.getAllCategories());
                                return "book/edit";
                        }
                }

                bookService.updateBook(book);
                return "redirect:/books";
        }

        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable long id) {
                bookService.getBookById(id)
                                .ifPresentOrElse(
                                                book -> bookService.deleteBookById(id),
                                                () -> {
                                                        throw new IllegalArgumentException("Book not found");
                                                });
                return "redirect:/books";
        }

        @PostMapping("/add-to-cart")
        public String addToCart(HttpSession session,
                        @RequestParam long id,
                        @RequestParam String name,
                        @RequestParam double price,
                        @RequestParam(defaultValue = "1") int quantity) {
                var cart = cartService.getCart(session);
                cart.addItems(new Item(id, name, price, quantity));
                cartService.updateCart(session, cart);
                return "redirect:/books";
        }

        @GetMapping("/detail/{id}")
        public String bookDetail(@PathVariable long id, Model model) {
                var book = bookService.getBookById(id);
                if (book.isEmpty()) {
                        throw new IllegalArgumentException("Book not found");
                }
                model.addAttribute("book", book.get());
                return "book/detail";
        }

        @GetMapping("/images/{filename:.+}")
        @ResponseBody
        public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
                try {
                        Path file = Paths.get(uploadDir).resolve(filename);
                        Resource resource = new UrlResource(file.toUri());
                        if (resource.exists() || resource.isReadable()) {
                                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + resource.getFilename() + "\"")
                                                .body(resource);
                        } else {
                                return ResponseEntity.notFound().build();
                        }
                } catch (MalformedURLException e) {
                        return ResponseEntity.badRequest().build();
                }
        }

        private String saveImage(MultipartFile file) throws IOException {
                // Create upload directory if it doesn't exist
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }

                // Generate unique filename
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String filename = UUID.randomUUID().toString() + extension;

                // Save file
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath);

                return filename;
        }
}