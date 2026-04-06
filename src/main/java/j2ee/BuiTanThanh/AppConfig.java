package j2ee.BuiTanThanh;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.entities.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public List<Book> getBooks() {
        var books = new ArrayList<Book>();

        var category = Category.builder().id(1L).name("Công nghệ thông tin").build();

        books.add(Book.builder()
                .id(1L)
                .title("Lập trình Web Spring Framework")
                .author("Ánh Nguyễn")
                .price(29.99)
                .category(category)
                .build());
        books.add(Book.builder()
                .id(2L)
                .title("Lập trình ứng dụng Java")
                .author("Huy Cường")
                .price(45.63)
                .category(category)
                .build());
        books.add(Book.builder()
                .id(3L)
                .title("Lập trình Web Spring Boot")
                .author("Xuân hân")
                .price(12.0)
                .category(category)
                .build());
        books.add(Book.builder()
                .id(4L)
                .title("Lập trình Web Spring MVC")
                .author("Ánh Nguyễn")
                .price(0.12)
                .category(category)
                .build());

        return books;
    }
}