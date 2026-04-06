package j2ee.BuiTanThanh.NguyenHoangMinh;

import j2ee.BuiTanThanh.entities.Category;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.services.UserService;
import j2ee.BuiTanThanh.validators.ValidCategoryIdValidator;
import j2ee.BuiTanThanh.validators.ValidUsernameValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * White Box Tests - Nguyễn Hoàng Minh
 * Phạm vi: ValidUsernameValidator + ValidCategoryIdValidator (TC_NHM_017 →
 * TC_NHM_020)
 * Công cụ: JUnit 5 + Mockito + ReflectionTestUtils
 */
@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private UserService userService;

    @Mock
    private ConstraintValidatorContext constraintContext;

    private ValidUsernameValidator usernameValidator;
    private ValidCategoryIdValidator categoryIdValidator;

    @BeforeEach
    void setUp() {
        usernameValidator = new ValidUsernameValidator();
        // Inject mock UserService vào validator (field @Autowired)
        ReflectionTestUtils.setField(usernameValidator, "userService", userService);

        categoryIdValidator = new ValidCategoryIdValidator();
    }

    // =========================================================
    // TC_NHM_017: ValidUsernameValidator trả về true khi username chưa tồn tại
    // Luồng code: userService.findByUsername(username).isEmpty() → true → valid
    // =========================================================
    @Test
    @DisplayName("TC_NHM_017 - isValid trả về true khi username chưa tồn tại trong hệ thống")
    void validateUsername_shouldReturnTrue_whenUsernameIsAvailable() {
        when(userService.findByUsername("newuser123")).thenReturn(Optional.empty());

        boolean result = usernameValidator.isValid("newuser123", constraintContext);

        assertThat(result).isTrue();
    }

    // =========================================================
    // TC_NHM_018: ValidUsernameValidator trả về false khi username đã tồn tại
    // Luồng code: userService.findByUsername(username).isEmpty() → false → invalid
    // =========================================================
    @Test
    @DisplayName("TC_NHM_018 - isValid trả về false khi username đã tồn tại trong hệ thống")
    void validateUsername_shouldReturnFalse_whenUsernameAlreadyExists() {
        User existingUser = User.builder().username("existinguser").build();
        when(userService.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        boolean result = usernameValidator.isValid("existinguser", constraintContext);

        assertThat(result).isFalse();
    }

    // =========================================================
    // TC_NHM_019: ValidCategoryIdValidator trả về true khi Category có id hợp lệ
    // Luồng code: category != null && category.getId() != null → true (pure logic)
    // =========================================================
    @Test
    @DisplayName("TC_NHM_019 - isValid trả về true khi Category có id không null")
    void validateCategoryId_shouldReturnTrue_whenCategoryHasValidId() {
        Category category = Category.builder().id(1L).name("Fiction").build();

        boolean result = categoryIdValidator.isValid(category, constraintContext);

        assertThat(result).isTrue();
    }

    // =========================================================
    // TC_NHM_020: ValidCategoryIdValidator trả về false khi Category là null
    // Luồng code: category == null → false (pure logic)
    // =========================================================
    @Test
    @DisplayName("TC_NHM_020 - isValid trả về false khi Category là null")
    void validateCategoryId_shouldReturnFalse_whenCategoryIsNull() {
        boolean result = categoryIdValidator.isValid(null, constraintContext);

        assertThat(result).isFalse();
    }
}
