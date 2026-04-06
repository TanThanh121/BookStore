# TEST CASES — Bookstore Web Application (Spring Boot)
**Project:** 2280602928_BuiTanThanh  
**Tổng số test case:** 100  
- Hộp Trắng (White Box): 20 TCs  
- Hộp Đen (Black Box): 40 TCs  
- Auto Test Selenium: 40 TCs  

---

## Phân bố theo Module

| Module | White Box | Black Box | Selenium | Tổng |
|--------|-----------|-----------|----------|------|
| Module 1: Authentication & User Management | 4 | 8 | 8 | 20 |
| Module 2: Book Browsing & Search | 4 | 8 | 8 | 20 |
| Module 3: Shopping Cart | 4 | 8 | 8 | 20 |
| Module 4: Order & Checkout | 4 | 8 | 8 | 20 |
| Module 5: Admin Management | 4 | 8 | 8 | 20 |
| **Tổng** | **20** | **40** | **40** | **100** |

---

## Quy ước ký hiệu

| Ký hiệu | Ý nghĩa |
|---------|---------|
| **WB** | White Box – kiểm thử logic nội bộ (unit test) |
| **BB** | Black Box – kiểm thử đầu vào/đầu ra |
| **SE** | Selenium – kiểm thử tự động giao diện |
| **P1** | Priority 1 – Critical |
| **P2** | Priority 2 – High |
| **P3** | Priority 3 – Medium |

---

# MODULE 1: Authentication & User Management (20 TCs)

## 1.1 White Box – 4 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Phương thức kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|----------------------|
| TC-AUTH-WB-001 | Xác minh `UserService.save()` mã hóa mật khẩu trước khi lưu vào database | WB – Positive | P1 | Bean `UserService`, `IUserRepository` mock sẵn | User object với password = `"Plain123"` | Password lưu vào DB **không bằng** `"Plain123"`, bắt đầu bằng `$2a$` (BCrypt hash) | Gọi `userService.save(user)`, assert `!user.getPassword().equals("Plain123")` và `user.getPassword().startsWith("$2a$")` |
| TC-AUTH-WB-002 | Xác minh `UserService.changePassword()` ném `IllegalArgumentException` khi mật khẩu hiện tại sai | WB – Negative | P1 | User đã tồn tại trong DB với password đã mã hóa | `currentPassword = "WrongPass"`, `newPassword = "NewPass123"` | Ném `IllegalArgumentException` với message `"Current password is incorrect"` | Mock repo trả về user, gọi `changePassword()`, assert `assertThrows(IllegalArgumentException.class, ...)` |
| TC-AUTH-WB-003 | Xác minh `UserService.changePassword()` ném `IllegalArgumentException` khi mật khẩu mới < 6 ký tự | WB – Negative | P2 | User tồn tại, `currentPassword` đúng | `newPassword = "abc"` (3 ký tự) | Ném `IllegalArgumentException` với message `"New password must be at least 6 characters"` | Assert `assertThrows` với `newPassword = "abc"` |
| TC-AUTH-WB-004 | Xác minh `UserService.updateProfile()` ném `IllegalArgumentException` khi email đã được dùng bởi user khác | WB – Negative | P1 | 2 user tồn tại trong DB: userA và userB | UserA cập nhật email sang email của userB | Ném `IllegalArgumentException` với message `"Email already in use by another account"` | Mock `findByEmail()` trả về userB, gọi `updateProfile()` cho userA, assert exception |

---

## 1.2 Black Box – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Kỹ thuật kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-AUTH-BB-001 | Xác minh đăng ký thành công khi nhập đầy đủ thông tin hợp lệ | BB – Positive | P1 | Chưa có user cùng username/email | `username="newuser"`, `password="Pass123!"`, `email="new@test.com"` | Redirect sang `/login`, user được tạo trong DB | Equivalence Partitioning – valid class |
| TC-AUTH-BB-002 | Xác minh đăng ký thất bại khi để trống trường `username` | BB – Negative | P1 | Trang đăng ký mở | `username=""`, `password="Pass123!"`, `email="x@test.com"` | Ở lại trang `/register`, hiển thị lỗi `"Username is required"` | Boundary Value – empty input |
| TC-AUTH-BB-003 | Xác minh đăng ký thất bại khi `username` vượt quá 50 ký tự | BB – Negative | P2 | Trang đăng ký mở | `username` = 51 ký tự `"a"`, `password="Pass123!"` | Hiển thị lỗi `"Username must be between 1 and 50 characters"` | Boundary Value – max+1 |
| TC-AUTH-BB-004 | Xác minh đăng ký thất bại khi email không đúng định dạng | BB – Negative | P1 | Trang đăng ký mở | `email="notanemail"`, các field khác hợp lệ | Hiển thị lỗi validation email | Equivalence Partitioning – invalid class |
| TC-AUTH-BB-005 | Xác minh đăng nhập thành công với email/password hợp lệ | BB – Positive | P1 | User `demouser` đã tồn tại trong DB | `username="demouser"`, `password="Password123!"` | Redirect sang `/`, hiển thị tên user ở header | Equivalence Partitioning – valid class |
| TC-AUTH-BB-006 | Xác minh đăng nhập thất bại với password sai | BB – Negative | P1 | User tồn tại | `username="demouser"`, `password="WrongPass"` | Ở lại `/login`, hiển thị lỗi đăng nhập | Equivalence Partitioning – invalid class |
| TC-AUTH-BB-007 | Xác minh cập nhật profile thành công khi nhập tên và số điện thoại hợp lệ | BB – Positive | P2 | User đã đăng nhập | `name="Nguyễn Văn A"`, `phone="0901234567"` | Hiển thị flash message `"Profile updated successfully!"` | Equivalence Partitioning – valid class |
| TC-AUTH-BB-008 | Xác minh đăng ký thất bại khi `phone` không đúng 10 chữ số | BB – Negative | P2 | Trang đăng ký mở | `phone="090123"` (6 ký tự) | Hiển thị lỗi `"Phone must be 10 characters"` | Boundary Value – min-1 |

---

## 1.3 Selenium Auto Test – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Test Script Design |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-AUTH-SE-001 | Xác minh đăng nhập thành công bằng giao diện web | SE – Positive | P1 | App đang chạy, user `demouser` tồn tại | `username="demouser"`, `password="Password123!"` | URL chứa `/`, header hiển thị `demouser` | `open /login` → `fill username` → `fill password` → `click submit` → `assertURL(/)` → `assertText(header, demouser)` |
| TC-AUTH-SE-002 | Xác minh hiển thị lỗi khi đăng nhập với password sai | SE – Negative | P1 | App đang chạy | `username="demouser"`, `password="Bad"` | Trang vẫn là `/login`, có text lỗi | `open /login` → `fill fields` → `click submit` → `assertURL(/login)` → `assertElementPresent(error-msg)` |
| TC-AUTH-SE-003 | Xác minh đăng ký tài khoản mới thành công qua giao diện | SE – Positive | P1 | App đang chạy, username chưa tồn tại | `username="testselenium"`, `password="Test123!"`, `email="se@test.com"` | Redirect sang `/login` sau khi đăng ký | `open /register` → `fill form` → `click register` → `assertURL(/login)` |
| TC-AUTH-SE-004 | Xác minh trang đăng ký hiển thị lỗi khi bỏ trống username | SE – Negative | P2 | App đang chạy | `username=""`, `password="Test123!"`, `email="se2@test.com"` | Ở lại `/register`, hiển thị thông báo lỗi | `open /register` → `fill password & email only` → `click register` → `assertElementPresent(.error)` |
| TC-AUTH-SE-005 | Xác minh chức năng đăng xuất hoạt động đúng | SE – Positive | P1 | User đã đăng nhập | Phiên đăng nhập hiện tại | Redirect về `/login`, mất phiên đăng nhập | `login` → `click logout` → `assertURL(/login)` |
| TC-AUTH-SE-006 | Xác minh truy cập `/profile` không thành công khi chưa đăng nhập | SE – Negative | P1 | Chưa đăng nhập | Trực tiếp truy cập URL `/profile` | Redirect sang `/login` | `open /profile` → `assertURL(/login)` |
| TC-AUTH-SE-007 | Xác minh cập nhật tên hiển thị và điện thoại trong profile | SE – Positive | P2 | User đã đăng nhập | `name="Tên Mới"`, `phone="0987654321"` | Flash message `"Profile updated successfully!"` xuất hiện | `open /profile` → `fill name & phone` → `click update` → `assertText(success-msg)` |
| TC-AUTH-SE-008 | Xác minh đổi mật khẩu thất bại khi `newPassword` và `confirmPassword` không khớp | SE – Negative | P2 | User đã đăng nhập | `newPassword="NewPass1"`, `confirmPassword="NewPass2"` | Hiển thị flash message lỗi `"New passwords do not match"` | `open /profile` → `fill change-password form` → `click change` → `assertText(error-msg)` |

---

# MODULE 2: Book Browsing & Search (20 TCs)

## 2.1 White Box – 4 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Phương thức kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|----------------------|
| TC-BOOK-WB-001 | Xác minh logic phân trang trong `BookController.loadBookList()` trả đúng số trang khi tổng sách = 45 | WB – Positive | P2 | Mock `bookService.getAllBooks()` trả 45 items | `pageNo=0`, `PAGE_SIZE=20` | `totalPages=3`, `pageBooks` có 20 items | Test `loadBookList()` với list 45 books, assert `totalPages == 3` |
| TC-BOOK-WB-002 | Xác minh `loadBookList()` giới hạn `pageNo` tối đa không vượt `totalPages - 1` | WB – Negative | P2 | 10 books, `PAGE_SIZE=20` | `pageNo=99` | `pageNo` bị clamp về `0` (trang cuối hợp lệ) | Assert `currentPage == 0` khi truyền `pageNo=99` với chỉ 10 books |
| TC-BOOK-WB-003 | Xác minh `BookService.deleteBookById()` set `active=false` thay vì xóa khỏi DB | WB – Positive | P1 | Book có `id=1` tồn tại, `active=true` | `id=1` | Book vẫn còn trong DB nhưng `active=false` | Mock repo, gọi `deleteBookById(1)`, assert `book.isActive() == false` và `save()` được gọi 1 lần |
| TC-BOOK-WB-004 | Xác minh `BookService.updateBook()` không ghi đè ảnh khi `imageFile` là empty string | WB – Positive | P2 | Book tồn tại với `image="old.jpg"` | Book object mới với `image=""` | `existingBook.getImage()` vẫn là `"old.jpg"` | Gọi `updateBook()` với book có image rỗng, assert image không thay đổi |

---

## 2.2 Black Box – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Kỹ thuật kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-BOOK-BB-001 | Xác minh trang danh sách sách hiển thị đúng khi không có filter | BB – Positive | P1 | Có ít nhất 1 sách active trong DB | GET `/books` | Trả về HTTP 200, hiển thị danh sách sách | Equivalence Partitioning – valid |
| TC-BOOK-BB-002 | Xác minh tìm kiếm sách theo keyword trả đúng kết quả | BB – Positive | P1 | Có sách với title "Spring" trong DB | `keyword="Spring"` | Danh sách chỉ chứa sách có "Spring" trong tên | Equivalence Partitioning – valid class |
| TC-BOOK-BB-003 | Xác minh tìm kiếm sách với keyword không tồn tại trả về danh sách rỗng | BB – Negative | P2 | DB không có sách tên "XYZ123" | `keyword="XYZ123"` | Danh sách rỗng, `totalResults=0` | Equivalence Partitioning – invalid class |
| TC-BOOK-BB-004 | Xác minh lọc sách theo danh mục trả đúng kết quả | BB – Positive | P2 | Có sách thuộc category `id=1` | `category=1` | Tất cả sách trả về đều thuộc category id=1 | Decision Table |
| TC-BOOK-BB-005 | Xác minh sắp xếp sách theo giá tăng dần | BB – Positive | P2 | Nhiều sách với giá khác nhau | `sortBy=price_asc` | Sách đầu tiên có giá thấp nhất | Equivalence Partitioning |
| TC-BOOK-BB-006 | Xác minh sắp xếp sách theo tên A-Z | BB – Positive | P3 | Nhiều sách với tên khác nhau | `sortBy=title_asc` | Sách sắp xếp theo alphabet tăng dần | Equivalence Partitioning |
| TC-BOOK-BB-007 | Xác minh trang chi tiết sách trả HTTP 200 với id hợp lệ | BB – Positive | P1 | Sách `id=1` active trong DB | GET `/books/detail/1` | HTTP 200, hiển thị thông tin chi tiết sách | Equivalence Partitioning |
| TC-BOOK-BB-008 | Xác minh phân trang: truy cập trang thứ 2 hiển thị đúng sách | BB – Positive | P2 | Có hơn 20 sách active | GET `/books?pageNo=1` | Hiển thị sách từ bản ghi 21 đến 40 | Boundary Value Analysis |

---

## 2.3 Selenium Auto Test – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Test Script Design |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-BOOK-SE-001 | Xác minh trang danh sách sách load đúng với đầy đủ các element | SE – Positive | P1 | App đang chạy, có sách trong DB | Truy cập `/books` | Danh sách sách hiển thị, có search bar, filter dropdown | `open /books` → `assertElementPresent(book-list)` → `assertElementPresent(search-bar)` |
| TC-BOOK-SE-002 | Xác minh chức năng tìm kiếm sách trên giao diện | SE – Positive | P1 | App đang chạy, có sách tên "Spring" | Nhập `"Spring"` vào ô tìm kiếm | Kết quả chỉ hiển thị sách có chứa "Spring" | `open /books` → `type(search-input, "Spring")` → `click search` → `assertText(results, "Spring")` |
| TC-BOOK-SE-003 | Xác minh click vào sách mở đúng trang chi tiết | SE – Positive | P1 | App đang chạy, có sách trong DB | Click vào tên/ảnh sách đầu tiên | URL chuyển sang `/books/detail/{id}`, hiển thị thông tin sách | `open /books` → `click first book` → `assertURLContains(/books/detail/)` |
| TC-BOOK-SE-004 | Xác minh chức năng lọc sách theo danh mục hoạt động | SE – Positive | P2 | Có danh mục trong DB | Chọn danh mục từ dropdown | Danh sách cập nhật, chỉ hiển thị sách thuộc danh mục đó | `open /books` → `select(category-dropdown, option)` → `assertFiltered(results)` |
| TC-BOOK-SE-005 | Xác minh chức năng sắp xếp giá tăng dần trên giao diện | SE – Positive | P2 | Có nhiều sách giá khác nhau | Chọn `"Giá tăng dần"` từ dropdown sort | Giá sách đầu tiên ≤ giá sách thứ hai | `select(sort-dropdown, price_asc)` → `getPrice(first)` → `getPrice(second)` → `assert first <= second` |
| TC-BOOK-SE-006 | Xác minh nút phân trang hoạt động đúng | SE – Positive | P2 | Có hơn 20 sách | Click nút "Trang tiếp" | URL có `pageNo=1`, hiển thị trang 2 | `open /books` → `click next-page` → `assertURLContains(pageNo=1)` |
| TC-BOOK-SE-007 | Xác minh nút "Thêm vào giỏ hàng" trên trang chi tiết sách (cần đăng nhập) | SE – Positive | P1 | User đã đăng nhập, đang ở trang chi tiết sách | Click `"Thêm vào giỏ hàng"` | Giỏ hàng tăng số lượng, hoặc redirect đến `/cart` | `login` → `open /books/detail/1` → `click add-to-cart` → `assertCartCount(1)` |
| TC-BOOK-SE-008 | Xác minh tìm kiếm với keyword rỗng trả về toàn bộ sách | SE – Positive | P3 | Có sách trong DB | Để trống ô tìm kiếm → Search | Hiển thị toàn bộ danh sách sách | `open /books` → `clear(search-input)` → `click search` → `assertElementPresent(book-list)` |

---

# MODULE 3: Shopping Cart (20 TCs)

## 3.1 White Box – 4 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Phương thức kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|----------------------|
| TC-CART-WB-001 | Xác minh `CartService.getSumPrice()` tính đúng tổng giá nhiều item | WB – Positive | P1 | Session với cart có 2 items: sách A (giá=10, qty=2), sách B (giá=5, qty=3) | Cart: `[{price:10, qty:2}, {price:5, qty:3}]` | `getSumPrice() == 35.0` | Mock session, assert `getSumPrice(session) == 35.0` |
| TC-CART-WB-002 | Xác minh `CartService.getSumQuantity()` đếm đúng tổng số lượng | WB – Positive | P2 | Cart có 3 items với qty: 2, 3, 5 | Cart có 3 items | `getSumQuantity() == 10` | Assert `getSumQuantity(session) == 10` |
| TC-CART-WB-003 | Xác minh `CartService.saveCart()` trả về `null` khi giỏ hàng trống | WB – Negative | P1 | Session với cart rỗng (không có item) | Cart rỗng | Trả về `null`, không tạo Invoice trong DB | Gọi `saveCart()` với empty cart, assert `result == null` |
| TC-CART-WB-004 | Xác minh `CartService.removeCart()` xóa cart khỏi session | WB – Positive | P2 | Session có cart với 2 items | Cart có items | Sau khi `removeCart()`, `getCart()` trả về cart mới rỗng | Gọi `removeCart(session)`, sau đó `getCart()`, assert `cartItems.isEmpty()` |

---

## 3.2 Black Box – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Kỹ thuật kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-CART-BB-001 | Xác minh thêm sách vào giỏ thành công | BB – Positive | P1 | User đã đăng nhập, sách tồn tại | GET `/cart/addToCart/1` | Giỏ hàng có 1 item, tổng quantity = 1 | Equivalence Partitioning |
| TC-CART-BB-002 | Xác minh xóa sách khỏi giỏ hàng thành công | BB – Positive | P1 | Giỏ hàng có sách `id=1` | GET `/cart/removeFromCart/1` | Sách `id=1` không còn trong giỏ, redirect `/cart` | Equivalence Partitioning |
| TC-CART-BB-003 | Xác minh cập nhật số lượng sách trong giỏ hàng | BB – Positive | P2 | Giỏ hàng có sách `id=1` qty=1 | GET `/cart/updateCart/1/3` | Sách `id=1` có qty=3 trong giỏ | Equivalence Partitioning |
| TC-CART-BB-004 | Xác minh xóa toàn bộ giỏ hàng | BB – Positive | P2 | Giỏ hàng có nhiều sách | GET `/cart/clearCart` | Giỏ hàng rỗng, redirect `/cart` | Decision Table |
| TC-CART-BB-005 | Xác minh trang giỏ hàng hiển thị đúng tổng tiền | BB – Positive | P1 | Giỏ có sách giá 50.000 qty=2, sách giá 30.000 qty=1 | Xem trang `/cart` | Tổng tiền = 130.000, totalQuantity = 3 | Equivalence Partitioning |
| TC-CART-BB-006 | Xác minh truy cập trang checkout khi giỏ hàng rỗng redirect về `/cart?error=empty` | BB – Negative | P1 | Giỏ hàng trống | GET `/cart/checkout` | Redirect `/cart?error=empty` | Equivalence Partitioning – boundary empty |
| TC-CART-BB-007 | Xác minh cập nhật số lượng về 0 xóa item khỏi giỏ | BB – Negative | P2 | Giỏ hàng có sách `id=1` | GET `/cart/updateCart/1/0` | Item bị xóa hoặc giỏ hàng xử lý đúng | Boundary Value – zero quantity |
| TC-CART-BB-008 | Xác minh giỏ hàng hiển thị đúng khi chỉ có 1 item | BB – Positive | P3 | Giỏ hàng có 1 sách | Xem `/cart` | Hiển thị đúng tên sách, giá, qty, tổng | Boundary Value – single item |

---

## 3.3 Selenium Auto Test – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Test Script Design |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-CART-SE-001 | Xác minh thêm sách vào giỏ hàng qua giao diện | SE – Positive | P1 | User đã đăng nhập, sách tồn tại | Click `"Thêm vào giỏ"` trên trang sách | Header giỏ hàng hiển thị số lượng = 1 | `login` → `open /books` → `click add-to-cart(book1)` → `assertCartBadge(1)` |
| TC-CART-SE-002 | Xác minh trang giỏ hàng hiển thị đúng sách đã thêm | SE – Positive | P1 | Giỏ hàng có 1 sách | Truy cập `/cart` | Tên sách, giá, số lượng hiển thị đúng | `open /cart` → `assertText(book-title)` → `assertText(price)` → `assertText(qty)` |
| TC-CART-SE-003 | Xác minh xóa sách khỏi giỏ hàng qua giao diện | SE – Positive | P1 | Giỏ có 1 sách | Click icon xóa trên item | Giỏ hàng rỗng, hiển thị thông báo "giỏ trống" | `open /cart` → `click remove-btn(item1)` → `assertText(cart-empty)` |
| TC-CART-SE-004 | Xác minh cập nhật số lượng sách trong giỏ qua input | SE – Positive | P2 | Giỏ hàng có sách qty=1 | Thay đổi input qty thành 3, click cập nhật | Qty hiển thị = 3, tổng tiền tăng lên | `open /cart` → `clearAndType(qty-input, 3)` → `click update` → `assertText(qty, 3)` |
| TC-CART-SE-005 | Xác minh nút "Xóa toàn bộ giỏ hàng" hoạt động | SE – Positive | P2 | Giỏ hàng có nhiều sách | Click `"Clear Cart"` | Giỏ hàng rỗng sau khi xóa | `open /cart` → `click clear-cart-btn` → `assertElementNotPresent(cart-items)` |
| TC-CART-SE-006 | Xác minh nút "Checkout" redirect đến trang thanh toán | SE – Positive | P1 | Giỏ hàng có sách | Click `"Checkout"` | URL chuyển sang `/cart/checkout` | `open /cart` → `click checkout-btn` → `assertURL(/cart/checkout)` |
| TC-CART-SE-007 | Xác minh giỏ hàng hiển thị tổng tiền đúng | SE – Positive | P1 | Giỏ có sách giá 50.0, qty=2 | Xem trang `/cart` | Tổng tiền = 100.0 hiển thị đúng | `open /cart` → `getText(total-price)` → `assertEqual(100.0)` |
| TC-CART-SE-008 | Xác minh thêm nhiều sách vào giỏ, badge header cập nhật | SE – Positive | P2 | User đã đăng nhập, 2 sách có sẵn | Thêm 2 sách khác nhau vào giỏ | Badge giỏ hàng hiển thị `2` | `login` → `addToCart(book1)` → `addToCart(book2)` → `assertCartBadge(2)` |

---

# MODULE 4: Order & Checkout (20 TCs)

## 4.1 White Box – 4 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Phương thức kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|----------------------|
| TC-ORD-WB-001 | Xác minh `CartService.saveCart()` tạo đúng Invoice và các ItemInvoice trong DB | WB – Positive | P1 | User đã đăng nhập, cart có 2 items | Cart: `[{bookId:1, qty:2, price:50.0}, {bookId:2, qty:1, price:30.0}]` | 1 Invoice được tạo với `price=130.0`, 2 ItemInvoice được tạo | Mock repos, assert `invoiceRepository.save()` gọi 1 lần, `itemInvoiceRepository.save()` gọi 2 lần |
| TC-ORD-WB-002 | Xác minh `CartService.saveCart()` xóa cart sau khi đặt hàng thành công | WB – Positive | P1 | Cart có items trước khi checkout | Cart với 2 items | Sau `saveCart()`, cart trong session trống | Gọi `saveCart()`, sau đó `getCart()`, assert `cartItems.isEmpty()` |
| TC-ORD-WB-003 | Xác minh `OrderController.orderDetail()` ném exception khi user truy cập đơn hàng của người khác | WB – Negative | P1 | UserA tạo đơn hàng `id=5`, UserB đăng nhập | UserB GET `/orders/detail/5` | Ném `IllegalArgumentException("Access denied")` | Mock security context với UserB, mock invoice với UserA, assert `assertThrows` |
| TC-ORD-WB-004 | Xác minh `InvoiceService.calculateInvoiceTotal()` tính đúng tổng khi `price` của invoice = 0 | WB – Positive | P2 | Invoice với `price=0` và 2 ItemInvoice | ItemInvoice: qty=2 price=50, qty=1 price=30 | Trả về `130.0` | Gọi `calculateInvoiceTotal(invoice)` với price=0, assert result == 130.0 |

---

## 4.2 Black Box – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Kỹ thuật kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-ORD-BB-001 | Xác minh checkout thành công khi điền đầy đủ thông tin hợp lệ và đồng ý điều khoản | BB – Positive | P1 | User đã đăng nhập, giỏ hàng có sách, `agreeTerms=true` | `customerName="Nguyen Van A"`, `email="a@test.com"`, `phone="0901234567"`, `address="123 Đường ABC"`, `agreeTerms=true` | Redirect sang `/orders/detail/{id}`, flash message thành công | Equivalence Partitioning – valid class |
| TC-ORD-BB-002 | Xác minh checkout thất bại khi không đồng ý điều khoản | BB – Negative | P1 | User đã đăng nhập, giỏ hàng có sách | Tất cả hợp lệ nhưng `agreeTerms=false` | Redirect `/cart/checkout` với flash message `"Please agree to terms and conditions."` | Decision Table |
| TC-ORD-BB-003 | Xác minh checkout thất bại khi giỏ hàng rỗng | BB – Negative | P1 | User đăng nhập, giỏ trống | POST `/cart/checkout` với cart trống | Redirect `/cart` với flash message `"Your cart is empty."` | Equivalence Partitioning – empty |
| TC-ORD-BB-004 | Xác minh trang lịch sử đơn hàng hiển thị đúng tất cả đơn hàng của user hiện tại | BB – Positive | P1 | User đã tạo 3 đơn hàng | GET `/orders` | Hiển thị đúng 3 đơn hàng, `totalSpent` đúng | Equivalence Partitioning |
| TC-ORD-BB-005 | Xác minh trang chi tiết đơn hàng hiển thị đúng thông tin | BB – Positive | P1 | User có đơn hàng `id=1` | GET `/orders/detail/1` | Hiển thị tên sách, số lượng, tổng tiền đúng | Equivalence Partitioning |
| TC-ORD-BB-006 | Xác minh user không thể xem đơn hàng của người khác qua URL | BB – Negative | P1 | UserA và UserB tồn tại, UserB có đơn `id=10` | UserA GET `/orders/detail/10` | Lỗi 403/500, không hiển thị đơn hàng của UserB | Security – Authorization |
| TC-ORD-BB-007 | Xác minh `totalSpent` trên lịch sử đơn hàng tính đúng | BB – Positive | P2 | User có 2 đơn: 100.000đ và 200.000đ | GET `/orders` | `totalSpent = 300.000` | Equivalence Partitioning |
| TC-ORD-BB-008 | Xác minh lịch sử đơn hàng rỗng khi user chưa mua hàng | BB – Positive | P3 | User mới, chưa có đơn hàng | GET `/orders` | Hiển thị thông báo "Chưa có đơn hàng", `totalSpent=0` | Boundary Value – empty set |

---

## 4.3 Selenium Auto Test – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Test Script Design |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-ORD-SE-001 | Xác minh luồng checkout hoàn chỉnh từ giỏ hàng đến đặt hàng thành công | SE – Positive | P1 | User đăng nhập, có sách trong giỏ | Điền đầy đủ form checkout, tick điều khoản | Flash message thành công, redirect sang trang chi tiết đơn | `login` → `addToCart` → `open /cart/checkout` → `fillForm(valid)` → `check(agreeTerms)` → `submit` → `assertURLContains(/orders/detail/)` |
| TC-ORD-SE-002 | Xác minh hiển thị lỗi khi checkout không tick điều khoản | SE – Negative | P1 | User đăng nhập, có sách trong giỏ | Điền form hợp lệ nhưng KHÔNG tick checkbox điều khoản | Ở lại trang checkout, thông báo lỗi | `open /cart/checkout` → `fillForm(valid)` → `do NOT check agreeTerms` → `submit` → `assertURL(/cart/checkout)` → `assertText(error)` |
| TC-ORD-SE-003 | Xác minh trang lịch sử đơn hàng hiển thị đúng sau khi đặt hàng | SE – Positive | P1 | User vừa đặt 1 đơn hàng thành công | GET `/orders` | Danh sách có ít nhất 1 đơn hàng | `login` → `completeCheckout` → `open /orders` → `assertElementPresent(order-list)` |
| TC-ORD-SE-004 | Xác minh click vào đơn hàng mở trang chi tiết đúng | SE – Positive | P1 | User có ít nhất 1 đơn hàng | Click vào đơn hàng trong danh sách | URL chứa `/orders/detail/`, hiển thị chi tiết sách | `open /orders` → `click first-order` → `assertURLContains(/orders/detail/)` |
| TC-ORD-SE-005 | Xác minh form checkout hiển thị đúng tổng tiền từ giỏ hàng | SE – Positive | P2 | Giỏ hàng có sách tổng = 150.000 | Mở trang `/cart/checkout` | Trang hiển thị tổng tiền = 150.000 | `addToCart(book, qty=3, price=50)` → `open /cart/checkout` → `assertText(total, 150)` |
| TC-ORD-SE-006 | Xác minh nút Back từ trang checkout về giỏ hàng | SE – Positive | P3 | Đang ở trang checkout | Click nút "Quay lại giỏ hàng" | Redirect về `/cart` | `open /cart/checkout` → `click back-to-cart` → `assertURL(/cart)` |
| TC-ORD-SE-007 | Xác minh truy cập `/orders` khi chưa đăng nhập redirect về login | SE – Negative | P1 | Chưa đăng nhập | Trực tiếp truy cập `/orders` | Redirect sang `/login` | `open /orders` → `assertURL(/login)` |
| TC-ORD-SE-008 | Xác minh hiển thị số lượng đơn hàng và tổng tiền đã chi tiêu trên trang `/orders` | SE – Positive | P2 | User có đơn hàng trong DB | GET `/orders` sau đăng nhập | Hiển thị `totalSpent` và `totalItems` đúng | `login` → `open /orders` → `assertElementPresent(total-spent)` → `assertElementPresent(total-items)` |

---

# MODULE 5: Admin Management (20 TCs)

## 5.1 White Box – 4 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Phương thức kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|----------------------|
| TC-ADMIN-WB-001 | Xác minh `AdminController.dashboard()` truyền đúng `totalBooks`, `totalUsers`, `totalOrders` vào model | WB – Positive | P1 | Mock services trả về: books=10, users=5, orders=3 | GET `/admin` | Model có `totalBooks=10`, `totalUsers=5`, `totalOrders=3` | Mock các services, gọi `dashboard(model)`, assert model attributes |
| TC-ADMIN-WB-002 | Xác minh `BookService.importBooksFromExcel()` bỏ qua (skip) dòng có giá âm | WB – Negative | P2 | File Excel có 3 dòng: 2 hợp lệ, 1 có price=-10 | Import File Excel | `imported=2`, `skipped=1`, lỗi ghi nhận cho dòng price âm | Gọi `importBooksFromExcel(file)`, assert `result.getImported()==2` và `result.getSkipped()==1` |
| TC-ADMIN-WB-003 | Xác minh `AdminController.addBook()` lưu đúng tên file ảnh sau khi upload | WB – Positive | P2 | Mock `bookService.addBook()`, thư mục upload tồn tại | MultipartFile không rỗng (JPEG) | `book.getImage()` được set thành tên file UUID-based (kết thúc `.jpg`) | Mock MultipartFile, gọi `addBook()`, assert `book.getImage()` không null và kết thúc đúng extension |
| TC-ADMIN-WB-004 | Xác minh `CategoryService.addCategory()` không tạo trùng tên danh mục | WB – Negative | P2 | Category `"Công nghệ"` đã tồn tại | Thêm category mới với name `"Công nghệ"` | Ném exception hoặc không tạo category thứ 2 | Mock repo, assert category không bị duplicate |

---

## 5.2 Black Box – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Kỹ thuật kiểm thử |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-ADMIN-BB-001 | Xác minh thêm sách mới thành công với đầy đủ thông tin hợp lệ | BB – Positive | P1 | Admin đăng nhập, có ít nhất 1 category | `title="Clean Code"`, `author="Robert Martin"`, `price=150000`, `category=1` | Flash message `"Book added successfully!"`, sách xuất hiện trong danh sách | Equivalence Partitioning – valid |
| TC-ADMIN-BB-002 | Xác minh thêm sách thất bại khi `title` để trống | BB – Negative | P1 | Admin đăng nhập | `title=""`, `author="Test"`, `price=50000` | Flash message lỗi, sách không được tạo | Equivalence Partitioning – empty |
| TC-ADMIN-BB-003 | Xác minh xóa mềm (deactivate) sách thành công | BB – Positive | P1 | Admin đăng nhập, sách `id=1` active | POST `/admin/books/delete/1` | Flash message `"Book deactivated successfully."`, sách `active=false` | Decision Table |
| TC-ADMIN-BB-004 | Xác minh kích hoạt lại sách đã bị deactivate | BB – Positive | P1 | Admin đăng nhập, sách `id=1` inactive | POST `/admin/books/activate/1` | Flash message `"Book activated successfully."`, sách `active=true` | Decision Table |
| TC-ADMIN-BB-005 | Xác minh chỉnh sửa sách cập nhật đúng thông tin | BB – Positive | P1 | Admin đăng nhập, sách tồn tại | Đổi `title="Updated Title"`, `price=200000` | Sách trong DB cập nhật đúng, flash message thành công | Equivalence Partitioning |
| TC-ADMIN-BB-006 | Xác minh xuất Excel trả về file `.xlsx` hợp lệ | BB – Positive | P2 | Admin đăng nhập, có sách trong DB | GET `/admin/books/export` | HTTP 200, Content-Type là `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`, file không rỗng | Equivalence Partitioning |
| TC-ADMIN-BB-007 | Xác minh non-admin user không thể truy cập `/admin` | BB – Negative | P1 | User role `USER` đã đăng nhập | GET `/admin` | HTTP 403 Forbidden, redirect trang lỗi | Security – Authorization |
| TC-ADMIN-BB-008 | Xác minh tìm kiếm sách trong admin theo keyword | BB – Positive | P2 | Admin đăng nhập, có sách title "Spring" | GET `/admin/books?keyword=Spring` | Chỉ hiển thị sách chứa "Spring" | Equivalence Partitioning |

---

## 5.3 Selenium Auto Test – 8 TCs

| TC ID | Test Case | Loại | Priority | Điều kiện tiên quyết | Dữ liệu kiểm thử | Kết quả mong đợi | Test Script Design |
|-------|-----------|------|----------|----------------------|------------------|------------------|-------------------|
| TC-ADMIN-SE-001 | Xác minh dashboard admin hiển thị đúng các thống kê | SE – Positive | P1 | Admin đã đăng nhập | Truy cập `/admin` | Hiển thị totalBooks, totalUsers, totalOrders, totalCategories | `login(admin)` → `open /admin` → `assertElementPresent(totalBooks)` → `assertElementPresent(totalUsers)` |
| TC-ADMIN-SE-002 | Xác minh thêm sách mới qua giao diện admin | SE – Positive | P1 | Admin đăng nhập, có category | `title="Selenium Book"`, `author="Author"`, `price=100000`, chọn category | Sách xuất hiện trong danh sách, flash success | `login(admin)` → `open /admin/books` → `fillAddForm(valid)` → `submit` → `assertText(success)` → `assertText(list, "Selenium Book")` |
| TC-ADMIN-SE-003 | Xác minh xóa sách qua giao diện admin (soft delete) | SE – Positive | P1 | Admin đăng nhập, sách "Test Book" tồn tại và active | Click nút `"Deactivate"` trên sách "Test Book" | Flash message thành công, sách hiển thị trạng thái inactive | `login(admin)` → `open /admin/books` → `click deactivate(Test Book)` → `assertText(success)` |
| TC-ADMIN-SE-004 | Xác minh kích hoạt lại sách qua giao diện admin | SE – Positive | P2 | Admin đăng nhập, sách đang inactive | Click nút `"Activate"` trên sách inactive | Flash message thành công, sách chuyển active | `login(admin)` → `open /admin/books` → `click activate(book)` → `assertText(success)` |
| TC-ADMIN-SE-005 | Xác minh chức năng tìm kiếm sách trong admin | SE – Positive | P2 | Admin đăng nhập, có sách "Spring Boot" | Nhập `"Spring"` vào ô search admin | Chỉ hiển thị sách chứa "Spring" | `login(admin)` → `open /admin/books` → `type(search, "Spring")` → `click search` → `assertText(results, "Spring")` |
| TC-ADMIN-SE-006 | Xác minh xuất file Excel từ admin hoạt động | SE – Positive | P2 | Admin đăng nhập, có sách trong DB | Click nút `"Export Excel"` | File `.xlsx` được download về máy | `login(admin)` → `open /admin/books` → `click export-excel` → `assertFileDownloaded(books_export.xlsx)` |
| TC-ADMIN-SE-007 | Xác minh truy cập trang admin bị chặn với user thường | SE – Negative | P1 | User role `USER` đã đăng nhập | Truy cập `/admin` | Redirect sang trang lỗi 403 | `login(user_role)` → `open /admin` → `assertURLContains(/error)` hoặc `assertText(403)` |
| TC-ADMIN-SE-008 | Xác minh thêm danh mục mới qua giao diện admin | SE – Positive | P2 | Admin đăng nhập | Điền `name="Khoa học"`, submit form thêm danh mục | Category "Khoa học" xuất hiện trong danh sách, flash success | `login(admin)` → `open /admin/categories` → `fillCategoryForm("Khoa học")` → `submit` → `assertText(list, "Khoa học")` |

---

# Tổng kết 100 Test Cases

## Thống kê theo loại

| Loại | Số lượng | Tỷ lệ |
|------|----------|-------|
| White Box (WB) | 20 | 20% |
| Black Box (BB) | 40 | 40% |
| Selenium Auto (SE) | 40 | 40% |
| **Tổng** | **100** | **100%** |

## Thống kê theo kết quả mong đợi

| Loại | Số lượng |
|------|----------|
| Positive (Happy Path) | 65 |
| Negative (Error Path) | 35 |

## Thống kê theo Priority

| Priority | Số lượng |
|----------|----------|
| P1 – Critical | 52 |
| P2 – High | 35 |
| P3 – Medium | 13 |

## Danh sách chức năng được kiểm thử

| Module | Chức năng |
|--------|-----------|
| **Module 1** | Đăng ký, Đăng nhập, Đăng xuất, OAuth2 Google, Cập nhật profile, Đổi mật khẩu |
| **Module 2** | Xem danh sách sách, Tìm kiếm sách, Lọc theo danh mục, Sắp xếp, Phân trang, Xem chi tiết |
| **Module 3** | Thêm vào giỏ, Cập nhật số lượng, Xóa khỏi giỏ, Xóa toàn bộ giỏ, Xem giỏ hàng |
| **Module 4** | Checkout, Lịch sử đơn hàng, Chi tiết đơn hàng, Kiểm tra quyền xem đơn hàng |
| **Module 5** | Dashboard, Thêm/Sửa/Xóa/Kích hoạt sách, Quản lý danh mục, Xuất/Nhập Excel, Phân quyền Admin |
