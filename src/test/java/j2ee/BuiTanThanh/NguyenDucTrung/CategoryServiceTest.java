package j2ee.BuiTanThanh.NguyenDucTrung;

import j2ee.BuiTanThanh.entities.Category;
import j2ee.BuiTanThanh.repositories.ICategoryRepository;
import j2ee.BuiTanThanh.services.CategoryService;
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
 * Phạm vi: CategoryService (TC_NDT_013 → TC_NDT_016)
 * Công cụ: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // =========================================================
    // TC_NDT_013: getAllCategories trả về toàn bộ danh mục
    // =========================================================
    @Test
    @DisplayName("TC_NDT_013 - getAllCategories trả về danh sách đầy đủ")
    void getAllCategories_shouldReturnAllCategories() {
        List<Category> mockCategories = List.of(
                Category.builder().id(1L).name("Science").build(),
                Category.builder().id(2L).name("Technology").build(),
                Category.builder().id(3L).name("Fiction").build());
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Category::getName)
                .containsExactly("Science", "Technology", "Fiction");
        verify(categoryRepository).findAll();
    }

    // =========================================================
    // TC_NDT_014: addCategory gọi repository.save để lưu danh mục mới
    // =========================================================
    @Test
    @DisplayName("TC_NDT_014 - addCategory gọi repository.save để lưu danh mục")
    void addCategory_shouldCallRepositorySave() {
        Category category = Category.builder().name("New Category").build();

        categoryService.addCategory(category);

        verify(categoryRepository, times(1)).save(category);
    }

    // =========================================================
    // TC_NDT_015: updateCategory cập nhật tên danh mục thành công
    // Luồng code: findById → setName → save
    // =========================================================
    @Test
    @DisplayName("TC_NDT_015 - updateCategory cập nhật tên danh mục đúng")
    void updateCategory_shouldUpdateName_whenCategoryExists() {
        Category existing = Category.builder().id(1L).name("Old Name").build();
        Category update = Category.builder().id(1L).name("New Name").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        categoryService.updateCategory(update);

        assertThat(existing.getName()).isEqualTo("New Name");
        verify(categoryRepository).save(existing);
    }

    // =========================================================
    // TC_NDT_016: deleteCategoryById xóa danh mục khỏi CSDL
    // Luồng code: categoryRepository.deleteById(id)
    // =========================================================
    @Test
    @DisplayName("TC_NDT_016 - deleteCategoryById gọi repository.deleteById để xóa danh mục")
    void deleteCategoryById_shouldCallDeleteById() {
        Long categoryId = 1L;

        categoryService.deleteCategoryById(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
