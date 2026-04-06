package j2ee.BuiTanThanh.NguyenHoangMinh;

import j2ee.BuiTanThanh.constants.Role;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.repositories.IRoleRepository;
import j2ee.BuiTanThanh.repositories.IUserRepository;
import j2ee.BuiTanThanh.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * White Box Tests - Nguyễn Hoàng Minh
 * Phạm vi: UserService (TC_NHM_001 → TC_NHM_010)
 * Công cụ: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    // =========================================================
    // TC_NHM_001: save() mã hóa mật khẩu trước khi lưu
    // Luồng code: BCryptPasswordEncoder.encode() → userRepository.save()
    // =========================================================
    @Test
    @DisplayName("TC_NHM_001 - save() mã hóa password trước khi lưu vào DB")
    void save_shouldEncodePasswordBeforeSaving() {
        User user = User.builder()
                .username("newuser")
                .password("plaintext123")
                .email("new@test.com")
                .roles(new HashSet<>())
                .build();
        j2ee.BuiTanThanh.entities.Role userRole = j2ee.BuiTanThanh.entities.Role.builder()
                .id(2L).name("USER").build();
        when(roleRepository.findRoleByName("USER")).thenReturn(userRole);

        userService.save(user);

        // Mật khẩu phải được mã hóa, khác với plain text
        assertThat(user.getPassword()).isNotEqualTo("plaintext123");
        // Mật khẩu mã hóa phải match bằng BCrypt
        assertThat(new BCryptPasswordEncoder().matches("plaintext123", user.getPassword())).isTrue();
        verify(userRepository).save(user);
    }

    // =========================================================
    // TC_NHM_002: findByUsername trả về Optional<User> khi username tồn tại
    // Luồng code: userRepository.findByUsername() — nhánh tìm thấy
    // =========================================================
    @Test
    @DisplayName("TC_NHM_002 - findByUsername trả về Optional<User> khi username tồn tại")
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        User user = User.builder().id(1L).username("admin").build();
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("admin");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("admin");
    }

    // =========================================================
    // TC_NHM_003: findByUsername trả về Optional.empty() khi username không tồn tại
    // Luồng code: userRepository.findByUsername() — nhánh không tìm thấy
    // =========================================================
    @Test
    @DisplayName("TC_NHM_003 - findByUsername trả về Optional.empty() khi không tìm thấy")
    void findByUsername_shouldReturnEmpty_whenUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }

    // =========================================================
    // TC_NHM_004: setDefaultRole gán role USER cho tài khoản
    // Luồng code: findByUsername →
    // user.getRoles().add(roleRepository.findRoleById(Role.USER.value))
    // =========================================================
    @Test
    @DisplayName("TC_NHM_004 - setDefaultRole gán role USER cho người dùng")
    void setDefaultRole_shouldAddUserRoleToUser() {
        User user = User.builder().username("testuser").roles(new HashSet<>()).build();
        j2ee.BuiTanThanh.entities.Role userRole = j2ee.BuiTanThanh.entities.Role.builder()
                .id(2L).name("USER").build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(roleRepository.findRoleById(Role.USER.value)).thenReturn(userRole);

        userService.setDefaultRole("testuser");

        assertThat(user.getRoles()).contains(userRole);
    }

    // =========================================================
    // TC_NHM_005: updateProfile cập nhật email, name, phone thành công
    // Luồng code: findByUsername → check email uniqueness (không trùng) → cập nhật
    // fields → save
    // =========================================================
    @Test
    @DisplayName("TC_NHM_005 - updateProfile cập nhật thông tin hợp lệ thành công")
    void updateProfile_shouldUpdateFields_whenDataIsValid() {
        User user = User.builder()
                .username("testuser").email("old@test.com").name("Old Name").phone("0111111111")
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());

        userService.updateProfile("testuser", "new@test.com", "New Name", "0987654321");

        assertThat(user.getEmail()).isEqualTo("new@test.com");
        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getPhone()).isEqualTo("0987654321");
        verify(userRepository).save(user);
    }

    // =========================================================
    // TC_NHM_006: updateProfile ném IllegalArgumentException khi email đã được dùng
    // Luồng code: findByEmail(newEmail).isPresent() → throw
    // IllegalArgumentException
    // =========================================================
    @Test
    @DisplayName("TC_NHM_006 - updateProfile ném IllegalArgumentException khi email trùng")
    void updateProfile_shouldThrowException_whenEmailAlreadyUsed() {
        User userA = User.builder().username("userA").email("a@test.com").build();
        User userB = User.builder().username("userB").email("b@test.com").build();
        when(userRepository.findByUsername("userB")).thenReturn(Optional.of(userB));
        // Email của userA đã tồn tại trong hệ thống
        when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(userA));

        assertThatThrownBy(() -> userService.updateProfile("userB", "a@test.com", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    // =========================================================
    // TC_NHM_007: changePassword đổi mật khẩu thành công
    // Luồng code: encoder.matches(current, stored) = true → encode(newPw) → save
    // =========================================================
    @Test
    @DisplayName("TC_NHM_007 - changePassword thành công khi mật khẩu hiện tại đúng")
    void changePassword_shouldUpdatePassword_whenCurrentPasswordIsCorrect() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String storedHash = encoder.encode("OldPass123");
        User user = User.builder().username("testuser").password(storedHash).build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        userService.changePassword("testuser", "OldPass123", "NewPass@123");

        // Mật khẩu mới phải được mã hóa và matches
        assertThat(encoder.matches("NewPass@123", user.getPassword())).isTrue();
        verify(userRepository).save(user);
    }

    // =========================================================
    // TC_NHM_008: changePassword ném ngoại lệ khi mật khẩu hiện tại sai
    // Luồng code: encoder.matches(wrong, stored) = false → throw
    // IllegalArgumentException
    // =========================================================
    @Test
    @DisplayName("TC_NHM_008 - changePassword ném IllegalArgumentException khi mật khẩu hiện tại sai")
    void changePassword_shouldThrowException_whenCurrentPasswordIsWrong() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String storedHash = encoder.encode("CorrectPass");
        User user = User.builder().username("testuser").password(storedHash).build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.changePassword("testuser", "WrongPassword", "NewPass@123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current password is incorrect");
    }

    // =========================================================
    // TC_NHM_009: changePassword ném ngoại lệ khi mật khẩu mới quá ngắn (< 6 ký tự)
    // Luồng code: newPassword.length() < 6 → throw IllegalArgumentException
    // =========================================================
    @Test
    @DisplayName("TC_NHM_009 - changePassword ném IllegalArgumentException khi mật khẩu mới quá ngắn")
    void changePassword_shouldThrowException_whenNewPasswordTooShort() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String storedHash = encoder.encode("CorrectPass");
        User user = User.builder().username("testuser").password(storedHash).build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.changePassword("testuser", "CorrectPass", "123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New password must be at least 6 characters");
    }

    // =========================================================
    // TC_NHM_010: countUsers trả về đúng tổng số người dùng
    // Luồng code: userRepository.count()
    // =========================================================
    @Test
    @DisplayName("TC_NHM_010 - countUsers trả về đúng tổng số người dùng")
    void countUsers_shouldReturnCorrectCount() {
        when(userRepository.count()).thenReturn(15L);

        long count = userService.countUsers();

        assertThat(count).isEqualTo(15L);
        verify(userRepository).count();
    }
}
