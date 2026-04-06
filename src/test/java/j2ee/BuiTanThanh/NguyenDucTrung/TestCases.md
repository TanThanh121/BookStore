# Test Cases - Nguyễn Đức Trung
**Loại kiểm thử:** Hộp Trắng (White Box)
**Số lượng:** 20 Test Cases
**Phạm vi:** BookService, CategoryService, CartService — kiểm thử logic nội bộ, nhánh điều kiện và luồng xử lý

---

## Chức năng: BookService

### Test Case TC_NDT_001
- **Summary:** `getAllBooks` trả về đúng danh sách sách với phân trang (pageNo=0, pageSize=5)
- **Phương thức kiểm thử:** `BookService.getAllBooks(0, 5, "id")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị dữ liệu có ít nhất 6 sách trong CSDL.
  - Bước 2: Gọi `bookService.getAllBooks(0, 5, "id")`.
  - Bước 3: Kiểm tra kích thước danh sách trả về.
- **Luồng code kiểm tra:** `IBookRepository.findAllBooks(0, 5, "id")` — nhánh phân trang đúng
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng 5 sách, `pageNo=0` tương ứng bản ghi đầu tiên.

---

### Test Case TC_NDT_002
- **Summary:** `getBookById` trả về sách khi ID tồn tại và sách đang active
- **Phương thức kiểm thử:** `BookService.getBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị một sách đang `active=true` với id hợp lệ.
  - Bước 2: Gọi `bookService.getBookById(id)`.
  - Bước 3: Kiểm tra `Optional` trả về.
- **Luồng code kiểm tra:** `IBookRepository.findActiveByIdWithCategory(id)` — nhánh tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Optional.of(book)` với đúng thông tin sách.

---

### Test Case TC_NDT_003
- **Summary:** `getBookById` trả về `Optional.empty()` khi ID không tồn tại
- **Phương thức kiểm thử:** `BookService.getBookById(9999L)`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có sách nào với id `9999`.
  - Bước 2: Gọi `bookService.getBookById(9999L)`.
  - Bước 3: Kiểm tra kết quả trả về.
- **Luồng code kiểm tra:** `IBookRepository.findActiveByIdWithCategory(9999L)` — nhánh không tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `Optional.empty()`, không ném ngoại lệ.

---

### Test Case TC_NDT_004
- **Summary:** `addBook` lưu sách mới thành công vào CSDL
- **Phương thức kiểm thử:** `BookService.addBook(book)`
- **Các bước thực hiện:**
  - Bước 1: Tạo đối tượng `Book` mới với `title`, `author`, `price` hợp lệ.
  - Bước 2: Gọi `bookService.addBook(book)`.
  - Bước 3: Truy vấn lại CSDL để kiểm tra sách đã được lưu.
- **Luồng code kiểm tra:** `bookRepository.save(book)` — luồng persist thành công
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách được lưu với `id` tự sinh, `active=true` theo mặc định.

---

### Test Case TC_NDT_005
- **Summary:** `updateBook` cập nhật đúng `title`, `author`, `price` khi sách tồn tại
- **Phương thức kiểm thử:** `BookService.updateBook(book)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách tồn tại với id hợp lệ.
  - Bước 2: Tạo đối tượng `book` mới với cùng id nhưng title mới `"Updated Title"`.
  - Bước 3: Gọi `bookService.updateBook(book)`.
  - Bước 4: Truy vấn lại và kiểm tra title.
- **Luồng code kiểm tra:** `bookRepository.findById(id)` → cập nhật fields → `bookRepository.save`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách trong CSDL có `title = "Updated Title"`.

---

### Test Case TC_NDT_006
- **Summary:** `updateBook` không ghi đè ảnh cũ khi ảnh mới rỗng
- **Phương thức kiểm thử:** `BookService.updateBook(book)` — nhánh `image == null || image.isEmpty()`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách đang có `image = "old_image.jpg"`.
  - Bước 2: Tạo đối tượng `book` cập nhật với `image = ""` (rỗng).
  - Bước 3: Gọi `bookService.updateBook(book)`.
  - Bước 4: Kiểm tra giá trị `image` trong CSDL.
- **Luồng code kiểm tra:** `if (book.getImage() != null && !book.getImage().isEmpty())` — nhánh `false`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** `image` giữ nguyên `"old_image.jpg"`, không bị ghi đè.

---

### Test Case TC_NDT_007
- **Summary:** `deleteBookById` đặt trạng thái `active=false` thay vì xóa vật lý
- **Phương thức kiểm thử:** `BookService.deleteBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách tồn tại với `active=true`.
  - Bước 2: Gọi `bookService.deleteBookById(id)`.
  - Bước 3: Truy vấn lại sách qua `bookRepository.findById(id)`.
- **Luồng code kiểm tra:** `book.setActive(false)` → `bookRepository.save(book)` — soft delete
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách vẫn còn trong CSDL nhưng `active=false`.

---

### Test Case TC_NDT_008
- **Summary:** `activateBookById` đặt lại `active=true` cho sách đã bị vô hiệu hóa
- **Phương thức kiểm thử:** `BookService.activateBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách với `active=false`.
  - Bước 2: Gọi `bookService.activateBookById(id)`.
  - Bước 3: Truy vấn lại và kiểm tra `active`.
- **Luồng code kiểm tra:** `book.setActive(true)` → `bookRepository.save(book)`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** `active=true` sau khi gọi phương thức.

---

### Test Case TC_NDT_009
- **Summary:** `searchActiveBook` trả về sách phù hợp với từ khóa (chỉ sách active)
- **Phương thức kiểm thử:** `BookService.searchActiveBook("Java")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị 2 sách active có tên chứa "Java" và 1 sách inactive.
  - Bước 2: Gọi `bookService.searchActiveBook("Java")`.
  - Bước 3: Kiểm tra danh sách kết quả.
- **Luồng code kiểm tra:** `bookRepository.searchActiveBook(keyword)` — lọc theo keyword và active
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về 2 sách active, không trả về sách inactive.

---

### Test Case TC_NDT_010
- **Summary:** `getBooksWithFilters` lọc đúng khi kết hợp cả keyword và category
- **Phương thức kiểm thử:** `BookService.getBooksWithFilters(categoryId, "Java")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách thuộc category X có tên "Java Programming" và sách thuộc category Y có tên "Java".
  - Bước 2: Gọi `bookService.getBooksWithFilters(categoryX.getId(), "Java")`.
  - Bước 3: Kiểm tra danh sách kết quả.
- **Luồng code kiểm tra:** `IBookRepository` — lọc kết hợp category + keyword
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Chỉ trả về sách thuộc category X có từ khóa "Java".

---

### Test Case TC_NDT_011
- **Summary:** `deleteBookById` với ID không tồn tại không ném ngoại lệ
- **Phương thức kiểm thử:** `BookService.deleteBookById(9999L)`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có sách với id `9999`.
  - Bước 2: Gọi `bookService.deleteBookById(9999L)`.
  - Bước 3: Kiểm tra không có exception.
- **Luồng code kiểm tra:** `bookRepository.findById(9999L).ifPresent(...)` — `Optional.empty`, lambda không chạy
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Không có ngoại lệ được ném, không thay đổi dữ liệu.

---

### Test Case TC_NDT_012
- **Summary:** `countBooks` trả về đúng tổng số sách trong hệ thống
- **Phương thức kiểm thử:** `BookService.countBooks()`
- **Các bước thực hiện:**
  - Bước 1: Biết trước số lượng sách hiện tại trong CSDL (ví dụ: 10).
  - Bước 2: Gọi `bookService.countBooks()`.
  - Bước 3: So sánh kết quả với số lượng thực tế.
- **Luồng code kiểm tra:** `bookRepository.count()`
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng con số bằng số bản ghi trong bảng `book`.

---

## Chức năng: CategoryService

### Test Case TC_NDT_013
- **Summary:** `getAllCategories` trả về toàn bộ danh mục
- **Phương thức kiểm thử:** `CategoryService.getAllCategories()`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị ít nhất 3 category trong CSDL.
  - Bước 2: Gọi `categoryService.getAllCategories()`.
  - Bước 3: Kiểm tra kích thước và nội dung.
- **Luồng code kiểm tra:** `categoryRepository.findAll()`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về danh sách đầy đủ các category.

---

### Test Case TC_NDT_014
- **Summary:** `addCategory` lưu danh mục mới thành công
- **Phương thức kiểm thử:** `CategoryService.addCategory(category)`
- **Các bước thực hiện:**
  - Bước 1: Tạo đối tượng `Category` với tên hợp lệ.
  - Bước 2: Gọi `categoryService.addCategory(category)`.
  - Bước 3: Truy vấn lại CSDL.
- **Luồng code kiểm tra:** `categoryRepository.save(category)`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Category được lưu với id tự sinh.

---

### Test Case TC_NDT_015
- **Summary:** `updateCategory` cập nhật tên danh mục thành công
- **Phương thức kiểm thử:** `CategoryService.updateCategory(category)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị category tồn tại.
  - Bước 2: Thay đổi tên rồi gọi `categoryService.updateCategory(category)`.
  - Bước 3: Truy vấn lại và kiểm tra.
- **Luồng code kiểm tra:** `categoryRepository.save(category)` — merge
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tên category được cập nhật trong CSDL.

---

### Test Case TC_NDT_016
- **Summary:** `deleteCategory` xóa danh mục không có sách liên kết
- **Phương thức kiểm thử:** `CategoryService.deleteCategory(id)`
- **Các bước thực hiện:**
  - Bước 1: Tạo category không có sách nào.
  - Bước 2: Gọi `categoryService.deleteCategory(id)`.
  - Bước 3: Kiểm tra category không còn tồn tại.
- **Luồng code kiểm tra:** `categoryRepository.deleteById(id)`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Category bị xóa khỏi CSDL.

---

## Chức năng: CartService

### Test Case TC_NDT_017
- **Summary:** `getCart` tạo giỏ hàng mới khi chưa có trong session
- **Phương thức kiểm thử:** `CartService.getCart(session)` — nhánh giỏ hàng chưa tồn tại
- **Các bước thực hiện:**
  - Bước 1: Tạo `HttpSession` mới (chưa có attribute `cart`).
  - Bước 2: Gọi `cartService.getCart(session)`.
  - Bước 3: Kiểm tra đối tượng cart trả về.
- **Luồng code kiểm tra:** `session.getAttribute("cart") == null` → tạo `Cart` mới
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Cart` mới rỗng, được lưu vào session.

---

### Test Case TC_NDT_018
- **Summary:** `getCart` trả về giỏ hàng hiện có khi đã tồn tại trong session
- **Phương thức kiểm thử:** `CartService.getCart(session)` — nhánh giỏ hàng đã tồn tại
- **Các bước thực hiện:**
  - Bước 1: Đặt attribute `cart` vào session với 1 sách.
  - Bước 2: Gọi `cartService.getCart(session)`.
  - Bước 3: Kiểm tra cart trả về có đúng là cart đã lưu.
- **Luồng code kiểm tra:** `session.getAttribute("cart") != null` → trả về cart cũ
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng giỏ hàng cũ với sách đã có.

---

### Test Case TC_NDT_019
- **Summary:** `getSumPrice` tính đúng tổng tiền của các sản phẩm trong giỏ hàng
- **Phương thức kiểm thử:** `CartService.getSumPrice(session)`
- **Các bước thực hiện:**
  - Bước 1: Thêm 2 sách vào giỏ: sách A giá 50.000 x2, sách B giá 30.000 x1.
  - Bước 2: Gọi `cartService.getSumPrice(session)`.
  - Bước 3: So sánh kết quả với 130.000.
- **Luồng code kiểm tra:** Tính tổng `price * quantity` cho mỗi item
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng tiền = `50.000 * 2 + 30.000 * 1 = 130.000`.

---

### Test Case TC_NDT_020
- **Summary:** `getSumQuantity` tính đúng tổng số lượng sách trong giỏ hàng
- **Phương thức kiểm thử:** `CartService.getSumQuantity(session)`
- **Các bước thực hiện:**
  - Bước 1: Thêm sách A số lượng 3 và sách B số lượng 2 vào giỏ.
  - Bước 2: Gọi `cartService.getSumQuantity(session)`.
  - Bước 3: Kiểm tra kết quả trả về.
- **Luồng code kiểm tra:** Tính tổng `quantity` của mỗi item trong cart
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng số lượng = `3 + 2 = 5`.

---

## Test Report

| STT | Test Case ID | Chức năng | Mô tả ngắn | Bản chất | Độ ưu tiên | Phương thức kiểm thử | Kết quả mong muốn | Kết quả thực tế | Trạng thái | Ngày test | Ghi chú |
|-----|-------------|-----------|------------|----------|------------|----------------------|-------------------|-----------------|------------|-----------|---------|
| 1 | TC_NDT_001 | BookService | getAllBooks phân trang | Positive | P1 | `getAllBooks(0,5,"id")` | Trả về 5 sách | | | | |
| 2 | TC_NDT_002 | BookService | getBookById tìm thấy | Positive | P1 | `getBookById(id)` | Optional chứa sách | | | | |
| 3 | TC_NDT_003 | BookService | getBookById không tìm thấy | Negative | P1 | `getBookById(9999L)` | Optional.empty() | | | | |
| 4 | TC_NDT_004 | BookService | addBook lưu thành công | Positive | P1 | `addBook(book)` | Sách được lưu vào DB | | | | |
| 5 | TC_NDT_005 | BookService | updateBook cập nhật fields | Positive | P1 | `updateBook(book)` | Title được cập nhật | | | | |
| 6 | TC_NDT_006 | BookService | updateBook không đổi ảnh nếu rỗng | Positive | P2 | `updateBook` nhánh image rỗng | Image giữ nguyên | | | | |
| 7 | TC_NDT_007 | BookService | deleteBookById soft delete | Positive | P1 | `deleteBookById(id)` | active=false | | | | |
| 8 | TC_NDT_008 | BookService | activateBookById | Positive | P2 | `activateBookById(id)` | active=true | | | | |
| 9 | TC_NDT_009 | BookService | searchActiveBook | Positive | P2 | `searchActiveBook("Java")` | Chỉ sách active | | | | |
| 10 | TC_NDT_010 | BookService | getBooksWithFilters | Positive | P2 | `getBooksWithFilters(catId, kw)` | Lọc đúng kết hợp | | | | |
| 11 | TC_NDT_011 | BookService | deleteBookById ID không tồn tại | Negative | P2 | `deleteBookById(9999L)` | Không ném exception | | | | |
| 12 | TC_NDT_012 | BookService | countBooks | Positive | P3 | `countBooks()` | Đúng tổng số | | | | |
| 13 | TC_NDT_013 | CategoryService | getAllCategories | Positive | P1 | `getAllCategories()` | Danh sách đầy đủ | | | | |
| 14 | TC_NDT_014 | CategoryService | addCategory | Positive | P1 | `addCategory(cat)` | Category được lưu | | | | |
| 15 | TC_NDT_015 | CategoryService | updateCategory | Positive | P2 | `updateCategory(cat)` | Tên được cập nhật | | | | |
| 16 | TC_NDT_016 | CategoryService | deleteCategory | Positive | P2 | `deleteCategory(id)` | Category bị xóa | | | | |
| 17 | TC_NDT_017 | CartService | getCart tạo mới | Positive | P1 | `getCart(newSession)` | Cart mới rỗng | | | | |
| 18 | TC_NDT_018 | CartService | getCart lấy cart cũ | Positive | P1 | `getCart(existingSession)` | Cart cũ | | | | |
| 19 | TC_NDT_019 | CartService | getSumPrice | Positive | P1 | `getSumPrice(session)` | 130.000 | | | | |
| 20 | TC_NDT_020 | CartService | getSumQuantity | Positive | P2 | `getSumQuantity(session)` | 5 | | | | |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **18**
- Negative: **2**
- Pass: ___
- Fail: ___
- Blocked: ___
