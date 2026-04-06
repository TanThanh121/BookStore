package j2ee.BuiTanThanh;

import j2ee.BuiTanThanh.entities.Book;
import j2ee.BuiTanThanh.entities.Category;
import j2ee.BuiTanThanh.repositories.IBookRepository;
import j2ee.BuiTanThanh.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements ApplicationRunner {

        private final IBookRepository bookRepository;
        private final ICategoryRepository categoryRepository;

        private static final String UPLOAD_DIR = "uploads/images";
        private static final String IMAGE_SOURCE_DIR = "imageSach";

        // 26 ảnh trong thư mục imageSach (tên chính xác khớp với file trong
        // uploads/images)
        private static final String[] IMAGE_FILES = {
                        "bay-mut-tet-nao_4338fbfe81024a9da2cd139d157cd34f_large.jpg",
                        "be-dan-dan-don-tet-ron-rang-du-xuan-nhon-nhip_70758922a7db40ac96e59b39210519a0_large.jpg",
                        "be-dan-dan-don-tet-ron-rang-ngoi-nha-long-lay_acf0a2adce054160a3ab3095b07ded18_large.jpg",
                        "chung_banh_giay_1_2019_08_26_16_15_38_81cebcebc1e34cb79575108a6b387bcf_a4da3ff004a948e28a64bc52855b1b2c_large.jpg",
                        "chuyen-nho-o-cho-lon_7ce6be8cedf24b4ca483160e2309753c_large.jpg",
                        "cozy-healing-nang-xuan-qua-goc-pho_f23396a321884597a8d2f38e8082d9d7_large.jpg",
                        "goi-banh-chung-nao_fe0de0395234493f830c28b163316799_large.jpg",
                        "hong-mao-dau-do-ra-ngo-don-xuan_f05b2b06c483491289882cb53bf824b0_large.jpg",
                        "le-tet-que-huong-tam-biet-chep-vang_6c83d8f622274e8ab07061c80d41805a_large.jpg",
                        "mon-ngon-sai-gon-tu-nha-ra-pho_efac768bfe19438f971ffb6458016a91_large.jpg",
                        "nhip-song-cho-lon_1b596be8724f4292944b0adc1dd42d14_large.jpg",
                        "noi-nieu-xong-chao_a75a8795551b4564bcd5ff2085d3251a_large.jpg",
                        "o-day-co-mot-con-en-2-db_b9b6881f2621428ea3514eba903de9d1_large.jpg",
                        "sai-gon-chuyen-doi-cua-pho-1-tb-2021_a87ebd92fff24f3aba5df7e18556112c_large.jpg",
                        "sai-gon-chuyen-doi-cua-pho-2-tb-2021_dd75ba89339549fb8128ee39b77e42d8_large.jpg",
                        "sai-gon-chuyen-doi-cua-pho-3-tb-2021_69d2d38b3a30488cac45f3d6a34380f7_large.jpg",
                        "tasty-menu-an-co-gu_6b8c1dbf1615404394152a53fdb853b3_large.jpg",
                        "tet-o-lang-dia-nguc_968b51c6f4b34dd9a1487cc8727b4e0c_large.jpg",
                        "tho-tet-danh-cho-thieu-nhi-sach-tet-mo-ra-ca-nha-ron-ra_af699b602c224a3c8f433143995be778_large.jpg",
                        "tho-tet-danh-cho-thieu-nhi_68e86b22bddf4416a625283567c7a668_large.jpg",
                        "thu-tet-cua-bac-ho_f4c7e2d09a49466b8b83e81dc6f11326_large.jpg",
                        "truyen-co-tich-dac-sac-ve-tet-sach-tet-mo-ra-ca-nha-ron-ra_94a32f672305494cb179dbb80f8762fb_large.jpg",
                        "tuy-but-hoi-ky-giai-thoai-tren-bao-sai-gon-xuan-xua-2_3d202ec911624fc0a11a7f5833df9a7c_large.jpg",
                        "tuy-but-hoi-ky-giai-thoai-tren-bao-xuan-sai-gon-xua_527545a2cd5d4536a81ec662a7b84f03_large.jpg",
                        "untitled-1_2lrq-l3_c1508534a09448c39b84827f61c424bf_large.jpg",
                        "vui-cung-sen-sun-tet-vui-no-am_5591eb4186694c50b1246bc60bb818f1_large.jpg"
        };

        // Map từ tên cũ (sai) → tên mới (đúng) để sửa DB
        private static final Map<String, String> FILENAME_FIXES = Map.of(
                        "be-dan-dan-don-tet-ron-rang-du-xuan-nhon-nhip_70758922a7db40ac96e59b39210519ba_large.jpg",
                        "be-dan-dan-don-tet-ron-rang-du-xuan-nhon-nhip_70758922a7db40ac96e59b39210519a0_large.jpg",

                        "be-dan-dan-don-tet-ron-rang-ngoi-nha-long-lay_acf0a2adce054160a3ab3095b07dedb6_large.jpg",
                        "be-dan-dan-don-tet-ron-rang-ngoi-nha-long-lay_acf0a2adce054160a3ab3095b07ded18_large.jpg",

                        "chung_banh_giay_1_2019_08_26_16_15_38_81cebcebc1e34cb79575108a6b387bcf_a4da3b6498674fac85a48bc01d4dbba8_large.jpg",
                        "chung_banh_giay_1_2019_08_26_16_15_38_81cebcebc1e34cb79575108a6b387bcf_a4da3ff004a948e28a64bc52855b1b2c_large.jpg",

                        "tho-tet-danh-cho-thieu-nhi-sach-tet-mo-ra-ca-nha-ron-ra_af699b602c224a3c8f436e1c13dcf40d_large.jpg",
                        "tho-tet-danh-cho-thieu-nhi-sach-tet-mo-ra-ca-nha-ron-ra_af699b602c224a3c8f433143995be778_large.jpg",

                        "truyen-co-tich-dac-sac-ve-tet-sach-tet-mo-ra-ca-nha-ron-ra_94a32f672305494cb72ab4f6e12eb826_large.jpg",
                        "truyen-co-tich-dac-sac-ve-tet-sach-tet-mo-ra-ca-nha-ron-ra_94a32f672305494cb179dbb80f8762fb_large.jpg",

                        "tuy-but-hoi-ky-giai-thoai-tren-bao-sai-gon-xuan-xua-2_3d202ec911624fc0a11a7f9b1f2ce25e_large.jpg",
                        "tuy-but-hoi-ky-giai-thoai-tren-bao-sai-gon-xuan-xua-2_3d202ec911624fc0a11a7f5833df9a7c_large.jpg");

        private static final Map<String, String> FILENAME_FIXES_2 = Map.of(
                        "tuy-but-hoi-ky-giai-thoai-tren-bao-xuan-sai-gon-xua_527545a2cd5d4536a81ec662f9ff2f64_large.jpg",
                        "tuy-but-hoi-ky-giai-thoai-tren-bao-xuan-sai-gon-xua_527545a2cd5d4536a81ec662a7b84f03_large.jpg");

        @Override
        @Transactional
        public void run(ApplicationArguments args) throws Exception {
                repairImageFilenames(); // luôn chạy để sửa tên file sai trong DB
                activateAllBooks(); // luôn chạy để fix sách cũ có active = false/null

                if (bookRepository.count() >= 50) {
                        log.info("DataSeeder: DB already has {} books, skipping.", bookRepository.count());
                        return;
                }

                log.info("DataSeeder: Copying images and seeding 100 books...");
                copyImages();
                seedData();
                log.info("DataSeeder: Done! Total books: {}", bookRepository.count());
        }

        @Transactional
        private void activateAllBooks() {
                List<Book> inactive = bookRepository.findAll().stream()
                                .filter(b -> !b.isActive())
                                .collect(java.util.stream.Collectors.toList());
                if (!inactive.isEmpty()) {
                        inactive.forEach(b -> b.setActive(true));
                        bookRepository.saveAll(inactive);
                        log.info("DataSeeder: Activated {} books that had active=false/null.", inactive.size());
                }
        }

        @Transactional
        private void repairImageFilenames() {
                java.util.HashMap<String, String> allFixes = new java.util.HashMap<>(FILENAME_FIXES);
                allFixes.putAll(FILENAME_FIXES_2);
                int fixed = 0;
                for (Map.Entry<String, String> entry : allFixes.entrySet()) {
                        List<Book> books = bookRepository.findAll().stream()
                                        .filter(b -> entry.getKey().equals(b.getImage()))
                                        .collect(java.util.stream.Collectors.toList());
                        for (Book b : books) {
                                b.setImage(entry.getValue());
                                bookRepository.save(b);
                                fixed++;
                        }
                }
                if (fixed > 0) {
                        log.info("DataSeeder: Repaired {} book image filenames.", fixed);
                }
        }

        private void copyImages() {
                try {
                        Path uploadPath = Paths.get(UPLOAD_DIR);
                        if (!Files.exists(uploadPath)) {
                                Files.createDirectories(uploadPath);
                        }
                        for (String imgName : IMAGE_FILES) {
                                Path dest = uploadPath.resolve(imgName);
                                if (!Files.exists(dest)) {
                                        try {
                                                ClassPathResource resource = new ClassPathResource(
                                                                "static/" + IMAGE_SOURCE_DIR + "/" + imgName);
                                                if (resource.exists()) {
                                                        try (InputStream is = resource.getInputStream()) {
                                                                Files.copy(is, dest,
                                                                                StandardCopyOption.REPLACE_EXISTING);
                                                        }
                                                }
                                        } catch (IOException e) {
                                                log.warn("Could not copy image {}: {}", imgName, e.getMessage());
                                        }
                                }
                        }
                        log.info("DataSeeder: Images copied to {}", UPLOAD_DIR);
                } catch (IOException e) {
                        log.error("DataSeeder: Failed to create upload dir", e);
                }
        }

        private String img(int index) {
                return IMAGE_FILES[index % IMAGE_FILES.length];
        }

        @Transactional
        private void seedData() {
                // ─── CATEGORIES ───────────────────────────────────────────────
                Category vanHoc = getOrCreate("Văn học Việt Nam");
                Category vanHocNN = getOrCreate("Văn học nước ngoài");
                Category lichSu = getOrCreate("Lịch sử - Địa lý");
                Category kyNang = getOrCreate("Kỹ năng sống");
                Category amThuc = getOrCreate("Ẩm thực");
                Category thieuNhi = getOrCreate("Thiếu nhi");
                Category tacPham = getOrCreate("Tác phẩm kinh điển");
                Category kinh = getOrCreate("Kinh tế - Kinh doanh");
                Category soTay = getOrCreate("Sổ tay - Tâm lý");
                Category khoaHoc = getOrCreate("Khoa học - Công nghệ");

                List<Book> books = new ArrayList<>();
                int i = 0;

                // ─── VĂN HỌC VIỆT NAM (12 cuốn) ──────────────────────────────
                books.add(book("Số Đỏ", "Vũ Trọng Phụng", 75000.0,
                                "Tiểu thuyết trào phúng, phản ánh xã hội thành thị Việt Nam thời Pháp thuộc.", img(i++),
                                vanHoc));
                books.add(book("Chí Phèo", "Nam Cao", 55000.0,
                                "Truyện ngắn kinh điển về người nông dân bị tha hóa trong xã hội cũ.", img(i++),
                                vanHoc));
                books.add(book("Vợ Nhặt", "Kim Lân", 50000.0,
                                "Truyện ngắn cảm động về con người trong nạn đói năm 1945.",
                                img(i++), vanHoc));
                books.add(book("Tắt Đèn", "Ngô Tất Tố", 60000.0,
                                "Tiểu thuyết tả thực về cuộc đời người phụ nữ nông dân trước cách mạng.", img(i++),
                                vanHoc));
                books.add(book("Lão Hạc", "Nam Cao", 55000.0,
                                "Truyện ngắn cảm động về một người nông dân giàu lòng nhân hậu.",
                                img(i++), vanHoc));
                books.add(book("Bỉ Vỏ", "Nguyên Hồng", 65000.0,
                                "Tiểu thuyết về cuộc đời đói khổ và tủi nhục của người phụ nữ bần cùng.", img(i++),
                                vanHoc));
                books.add(book("Chiếc Lược Ngà", "Nguyễn Quang Sáng", 58000.0,
                                "Truyện ngắn cảm động về tình cha con trong chiến tranh.", img(i++), vanHoc));
                books.add(book("Những Ngôi Sao Xa Xôi", "Lê Minh Khuê", 60000.0,
                                "Ba cô gái thanh niên xung phong dũng cảm trên tuyến đường Trường Sơn.", img(i++),
                                vanHoc));
                books.add(book("Làng", "Kim Lân", 52000.0,
                                "Truyện ngắn về tình yêu quê hương sâu sắc của người nông dân kháng chiến.", img(i++),
                                vanHoc));
                books.add(book("Mảnh Trăng Cuối Rừng", "Nguyễn Minh Châu", 62000.0,
                                "Truyện ngắn lãng mạn về thế hệ trẻ trong những năm kháng chiến chống Mỹ.", img(i++),
                                vanHoc));
                books.add(book("Rừng Xà Nu", "Nguyễn Trung Thành", 58000.0,
                                "Truyện ngắn tráng lệ về làng Xô Man kiên cường chống giặc.", img(i++), vanHoc));
                books.add(book("Đất Nước Đứng Lên", "Nguyên Ngọc", 70000.0,
                                "Tiểu thuyết sử thi về cuộc đấu tranh của người Tây Nguyên.", img(i++), vanHoc));

                // ─── VĂN HỌC NƯỚC NGOÀI (15 cuốn) ────────────────────────────
                books.add(book("Đắc Nhân Tâm", "Dale Carnegie", 108000.0,
                                "Cuốn sách kinh điển về nghệ thuật giao tiếp và ứng xử, bán chạy nhất mọi thời đại.",
                                img(i++),
                                vanHocNN));
                books.add(book("Nhà Giả Kim", "Paulo Coelho", 99000.0,
                                "Hành trình tìm kiếm kho báu và ý nghĩa cuộc sống của chàng trai trẻ Santiago.",
                                img(i++), vanHocNN));
                books.add(book("Tôi Tài Giỏi Bạn Cũng Thế", "Adam Khoo", 120000.0,
                                "Bí quyết học tập và thành công của thần đồng Adam Khoo.", img(i++), vanHocNN));
                books.add(book("Hoàng Tử Bé", "Antoine de Saint-Exupéry", 89000.0,
                                "Câu chuyện cổ tích dành cho người lớn về tình bạn, tình yêu và ý nghĩa cuộc sống.",
                                img(i++),
                                vanHocNN));
                books.add(book("Những Người Khốn Khổ", "Victor Hugo", 185000.0,
                                "Kiệt tác văn học Pháp về công lý, tình yêu và nhân phẩm con người.", img(i++),
                                vanHocNN));
                books.add(book("Tội Ác Và Hình Phạt", "Fyodor Dostoevsky", 150000.0,
                                "Tiểu thuyết tâm lý khai thác nội tâm phức tạp của con người sau một tội ác.", img(i++),
                                vanHocNN));
                books.add(book("Người Mẹ", "Maxim Gorky", 95000.0,
                                "Tiểu thuyết về người mẹ kiên cường theo con tham gia phong trào cách mạng.", img(i++),
                                vanHocNN));
                books.add(book("Chiến Tranh Và Hòa Bình", "Leo Tolstoy", 220000.0,
                                "Sử thi vĩ đại về nước Nga thời Napoleon, tình yêu và số phận con người.", img(i++),
                                vanHocNN));
                books.add(book("Don Quixote", "Miguel de Cervantes", 175000.0,
                                "Câu chuyện hài hước và sâu sắc về hiệp sĩ Don Quixote mộng mơ.", img(i++), vanHocNN));
                books.add(book("Giết Con Chim Nhại", "Harper Lee", 130000.0,
                                "Tiểu thuyết đoạt giải Pulitzer về công lý và lòng dũng cảm ở miền Nam nước Mỹ.",
                                img(i++), vanHocNN));
                books.add(book("Một Trăm Năm Cô Đơn", "Gabriel García Márquez", 160000.0,
                                "Kiệt tác chủ nghĩa hiện thực huyền ảo về gia đình Buendía qua bảy thế hệ.", img(i++),
                                vanHocNN));
                books.add(book("Bố Già", "Mario Puzo", 145000.0,
                                "Tiểu thuyết huyền thoại về gia đình mafia Corleone đầy quyền lực.", img(i++),
                                vanHocNN));
                books.add(book("Cuốn Theo Chiều Gió", "Margaret Mitchell", 195000.0,
                                "Thiên tình sử kỳ vĩ về Scarlett O'Hara trong cuộc Nội chiến Mỹ.", img(i++), vanHocNN));
                books.add(book("Mắt Biếc", "Nguyễn Nhật Ánh", 105000.0,
                                "Câu chuyện tình yêu ngây thơ và đau xót thời thơ ấu của chàng trai tên Ngạn.",
                                img(i++), vanHocNN));
                books.add(book("Cho Tôi Xin Một Vé Đi Tuổi Thơ", "Nguyễn Nhật Ánh", 95000.0,
                                "Hành trình trở về miền ký ức tuổi thơ trong sáng, hồn nhiên và đáng yêu.", img(i++),
                                vanHocNN));

                // ─── LỊCH SỬ - ĐỊA LÝ (10 cuốn) ─────────────────────────────
                books.add(book("Lịch Sử Việt Nam Bằng Tranh - Thời Văn Lang", "Trần Bạch Đằng", 88000.0,
                                "Bộ sách tranh tái hiện các thời kỳ lịch sử Việt Nam sinh động và hấp dẫn.", img(i++),
                                lichSu));
                books.add(book("Đại Việt Sử Ký Toàn Thư", "Ngô Sĩ Liên", 250000.0,
                                "Bộ chính sử đồ sộ ghi lại toàn bộ lịch sử Việt Nam từ thời thượng cổ.", img(i++),
                                lichSu));
                books.add(book("Hỏi Đáp Lịch Sử Việt Nam", "Nhiều Tác Giả", 75000.0,
                                "Tổng hợp những câu hỏi đáp thú vị về lịch sử dân tộc Việt Nam.", img(i++), lichSu));
                books.add(book("Người Hà Nội", "Nguyễn Khải", 80000.0,
                                "Hồi ký và bút ký về con người, văn hóa đất Hà Nội qua nhiều thập kỷ.", img(i++),
                                lichSu));
                books.add(book("Sài Gòn Chuyện Đời Của Phố", "Phạm Công Luận", 120000.0,
                                "Tập sách kể chuyện về những góc phố, con người và ký ức đất Sài Gòn.", img(i++),
                                lichSu));
                books.add(book("Hà Nội Băm Sáu Phố Phường", "Thạch Lam", 75000.0,
                                "Tác phẩm độc đáo về phong vị ẩm thực và không khí Hà Nội xưa.", img(i++), lichSu));
                books.add(book("Sapiens: Lược Sử Loài Người", "Yuval Noah Harari", 180000.0,
                                "Hành trình 70.000 năm của loài người từ thời đồ đá đến thời đại số.", img(i++),
                                lichSu));
                books.add(book("Homo Deus: Lược Sử Tương Lai", "Yuval Noah Harari", 175000.0,
                                "Nhà sử học dự đoán tương lai của loài người trong kỷ nguyên công nghệ.", img(i++),
                                lichSu));
                books.add(book("Súng, Vi Trùng Và Thép", "Jared Diamond", 165000.0,
                                "Giải thích tại sao một số nền văn minh lại chinh phục thế giới.", img(i++), lichSu));
                books.add(book("Địa Chí Văn Hóa Thành Phố Hồ Chí Minh", "Nhiều Tác Giả", 195000.0,
                                "Công trình nghiên cứu toàn diện về văn hóa, lịch sử vùng đất Sài Gòn - TP.HCM.",
                                img(i++), lichSu));

                // ─── KỸ NĂNG SỐNG (12 cuốn) ───────────────────────────────────
                books.add(book("7 Thói Quen Hiệu Quả", "Stephen R. Covey", 150000.0,
                                "Bảy thói quen giúp bạn trở thành người hiệu quả cao trong công việc và cuộc sống.",
                                img(i++), kyNang));
                books.add(book("Nghĩ Giàu Và Làm Giàu", "Napoleon Hill", 120000.0,
                                "Nghiên cứu bí quyết thành công của 500 người giàu nhất nước Mỹ.", img(i++), kyNang));
                books.add(book("Sức Mạnh Của Hiện Tại", "Eckhart Tolle", 135000.0,
                                "Hướng dẫn thực hành sống trong hiện tại để đạt sự bình an nội tâm.", img(i++),
                                kyNang));
                books.add(book("Atomic Habits", "James Clear", 160000.0,
                                "Phương pháp xây dựng thói quen tốt và loại bỏ thói quen xấu một cách khoa học.",
                                img(i++), kyNang));
                books.add(book("Mindset: Tâm Lý Học Thành Công", "Carol S. Dweck", 140000.0,
                                "Khám phá tư duy tăng trưởng giúp bạn phát triển bản thân không giới hạn.", img(i++),
                                kyNang));
                books.add(book("Deep Work: Làm Việc Sâu", "Cal Newport", 145000.0,
                                "Quy tắc thành công trong thế giới phân tâm - tập trung để tạo ra giá trị.", img(i++),
                                kyNang));
                books.add(book("Đọc Vị Bất Kỳ Ai", "David J. Lieberman", 95000.0,
                                "Nghệ thuật đọc hiểu hành vi và tâm lý của người đối diện.", img(i++), kyNang));
                books.add(book("Người Không Đánh Mất Bản Thân", "Nhiều Tác Giả", 89000.0,
                                "Hành trình tìm lại chính mình trong cuộc sống hiện đại nhiều áp lực.", img(i++),
                                kyNang));
                books.add(book("Dám Bị Ghét", "Ichiro Kishimi & Fumitake Koga", 130000.0,
                                "Triết học Adler qua cuộc đối thoại giữa triết gia và chàng thanh niên.", img(i++),
                                kyNang));
                books.add(book("Ikigai: Bí Quyết Trường Thọ Của Người Nhật", "Héctor Garcia", 110000.0,
                                "Khám phá lý do sống và hạnh phúc theo triết lý Nhật Bản.", img(i++), kyNang));
                books.add(book("Muốn Thành Công Hãy Thôi Bao Biện", "Brian Tracy", 99000.0,
                                "Ngừng tự bào chữa và bắt đầu hành động để thay đổi cuộc đời bạn.", img(i++), kyNang));
                books.add(book("Tư Duy Nhanh Và Chậm", "Daniel Kahneman", 170000.0,
                                "Khám phá hai hệ thống tư duy của não bộ và cách chúng ảnh hưởng đến quyết định.",
                                img(i++), kyNang));

                // ─── ẨM THỰC (8 cuốn) ─────────────────────────────────────────
                books.add(book("Bếp Gia Đình - Món Ngon Mỗi Ngày", "Nhiều Tác Giả", 85000.0,
                                "500 công thức nấu ăn đơn giản, ngon miệng cho bữa cơm gia đình.", img(i++), amThuc));
                books.add(book("Nghệ Thuật Nấu Phở", "Triệu Thị Chơi", 78000.0,
                                "Bí quyết nấu nước dùng trong, ngọt và các bí quyết gia truyền của món phở.", img(i++),
                                amThuc));
                books.add(book("Món Ngon Sài Gòn Từ Nhà Ra Phố", "Nhiều Tác Giả", 95000.0,
                                "Khám phá ẩm thực đường phố Sài Gòn phong phú và độc đáo.", img(i++), amThuc));
                books.add(book("Cẩm Nang Nấu Ăn Việt Nam", "Thanh Hương", 120000.0,
                                "Toàn bộ các món ăn truyền thống ba miền Bắc - Trung - Nam Việt Nam.", img(i++),
                                amThuc));
                books.add(book("Bánh Việt Nam", "Nhiều Tác Giả", 88000.0,
                                "Tổng hợp công thức làm các loại bánh truyền thống độc đáo của Việt Nam.", img(i++),
                                amThuc));
                books.add(book("Nồi Niêu Xoong Chảo", "Nhiều Tác Giả", 79000.0,
                                "Bí quyết chọn và sử dụng dụng cụ bếp đúng cách, kết hợp với các công thức nấu ăn.",
                                img(i++), amThuc));
                books.add(book("Tasty Menu: Ăn Có Gu", "Nhiều Tác Giả", 95000.0,
                                "Thực đơn chuẩn nhà hàng với các món ăn tinh tế và cách trình bày bắt mắt.", img(i++),
                                amThuc));
                books.add(book("Ăn Sạch Sống Khỏe", "Nhiều Tác Giả", 82000.0,
                                "Hướng dẫn chế độ dinh dưỡng lành mạnh và các công thức ăn uống tốt cho sức khỏe.",
                                img(i++), amThuc));

                // ─── THIẾU NHI (10 cuốn) ──────────────────────────────────────
                books.add(book("Doremon - Bộ Truyện Ngắn", "Fujiko F. Fujio", 25000.0,
                                "Những cuộc phiêu lưu thú vị của Nobita và chú mèo máy Doremon.", img(i++), thieuNhi));
                books.add(book("Harry Potter Và Hòn Đá Phù Thủy", "J.K. Rowling", 145000.0,
                                "Cuộc phiêu lưu đầu tiên của cậu bé phù thủy Harry Potter tại trường Hogwarts.",
                                img(i++), thieuNhi));
                books.add(book("Dế Mèn Phiêu Lưu Ký", "Tô Hoài", 65000.0,
                                "Cuộc phiêu lưu kỳ thú của chú dế mèn dũng cảm qua nhiều vùng đất lạ.", img(i++),
                                thieuNhi));
                books.add(book("Kính Vạn Hoa", "Nguyễn Nhật Ánh", 75000.0,
                                "Bộ truyện về những cuộc phiêu lưu hồn nhiên và thú vị của lũ trẻ làng quê.", img(i++),
                                thieuNhi));
                books.add(book("Thơ Tết Dành Cho Thiếu Nhi", "Nhiều Tác Giả", 55000.0,
                                "Tuyển tập thơ Tết trong sáng, vui tươi dành riêng cho các em thiếu nhi.", img(i++),
                                thieuNhi));
                books.add(book("Truyện Cổ Tích Đặc Sắc Việt Nam", "Nhiều Tác Giả", 68000.0,
                                "Tuyển tập những câu chuyện cổ tích hay nhất, ý nghĩa nhất của dân tộc Việt Nam.",
                                img(i++), thieuNhi));
                books.add(book("Bé Dan Dan Đón Tết Rộn Ràng", "Nhiều Tác Giả", 45000.0,
                                "Câu chuyện sinh động về bé Dan Dan háo hức đón Tết cùng gia đình.", img(i++),
                                thieuNhi));
                books.add(book("Hồng Mao Đầu Đỏ Ra Ngõ Đón Xuân", "Nhiều Tác Giả", 48000.0,
                                "Truyện tranh màu sắc tươi vui về chú gà hồng mao chào đón mùa xuân.", img(i++),
                                thieuNhi));
                books.add(book("Vui Cùng Sen Sun Tết Vui No Ấm", "Nhiều Tác Giả", 52000.0,
                                "Sách hình ảnh tươi sáng kể chuyện gia đình chuẩn bị đón Tết đầm ấm.", img(i++),
                                thieuNhi));
                books.add(book("Bách Khoa Toàn Thư Cho Trẻ Em", "Nhiều Tác Giả", 250000.0,
                                "Bộ sách tổng hợp kiến thức khoa học, tự nhiên và xã hội dành cho trẻ em.", img(i++),
                                thieuNhi));

                // ─── TÁC PHẨM KINH ĐIỂN (8 cuốn) ────────────────────────────
                books.add(book("Kiều - Đoạn Trường Tân Thanh", "Nguyễn Du", 89000.0,
                                "Truyện thơ Nôm nổi tiếng nhất Việt Nam kể về cuộc đời truân chuyên của Thúy Kiều.",
                                img(i++),
                                tacPham));
                books.add(book("Nam Quốc Sơn Hà - Tuyển Tập Thơ Văn Yêu Nước", "Nhiều Tác Giả", 85000.0,
                                "Tuyển tập những bài thơ văn yêu nước hào hùng nhất trong lịch sử dân tộc.", img(i++),
                                tacPham));
                books.add(book("Odyssey", "Homer", 130000.0,
                                "Sử thi Hy Lạp cổ đại kể về hành trình trở về đầy gian nan của Odysseus.", img(i++),
                                tacPham));
                books.add(book("Hamlet", "William Shakespeare", 95000.0,
                                "Bi kịch nổi tiếng nhất của Shakespeare về hoàng tử Đan Mạch trả thù cho cha.",
                                img(i++), tacPham));
                books.add(book("Romeo Và Juliet", "William Shakespeare", 85000.0,
                                "Bi kịch tình yêu vĩ đại nhất mọi thời đại của hai gia đình thù địch.", img(i++),
                                tacPham));
                books.add(book("Thần Thoại Hy Lạp", "Nhiều Tác Giả", 110000.0,
                                "Tuyển tập những câu chuyện thần thoại Hy Lạp hấp dẫn và ý nghĩa.", img(i++), tacPham));
                books.add(book("Đông Chu Liệt Quốc", "Phùng Mộng Long", 185000.0,
                                "Tiểu thuyết lịch sử đồ sộ về thời kỳ Xuân Thu Chiến Quốc của Trung Quốc.", img(i++),
                                tacPham));
                books.add(book("Tam Quốc Diễn Nghĩa", "La Quán Trung", 220000.0,
                                "Tiểu thuyết lịch sử vĩ đại về cuộc tranh hùng của ba nước Ngụy, Thục, Ngô.", img(i++),
                                tacPham));

                // ─── KINH TẾ - KINH DOANH (10 cuốn) ─────────────────────────
                books.add(book("Cha Giàu Cha Nghèo", "Robert T. Kiyosaki", 130000.0,
                                "Bài học về tài chính cá nhân và đầu tư từ người cha giàu và người cha nghèo.",
                                img(i++), kinh));
                books.add(book("Khởi Nghiệp Tinh Gọn", "Eric Ries", 145000.0,
                                "Phương pháp khởi nghiệp hiệu quả dựa trên vòng lặp xây dựng - đo lường - học hỏi.",
                                img(i++), kinh));
                books.add(book("Zero To One", "Peter Thiel", 150000.0,
                                "Bài học từ nhà đồng sáng lập PayPal về xây dựng công ty startup đột phá.", img(i++),
                                kinh));
                books.add(book("Từ Tốt Đến Vĩ Đại", "Jim Collins", 165000.0,
                                "Nghiên cứu những yếu tố giúp các công ty bình thường trở thành xuất sắc.", img(i++),
                                kinh));
                books.add(book("Chiến Lược Đại Dương Xanh", "W. Chan Kim", 155000.0,
                                "Tạo ra không gian thị trường mới nơi không có cạnh tranh để tăng trưởng vượt bậc.",
                                img(i++), kinh));
                books.add(book("Tư Duy Triệu Phú", "T. Harv Eker", 115000.0,
                                "Bí mật trong não bộ người giàu để xây dựng tư duy tài chính thành công.", img(i++),
                                kinh));
                books.add(book("Quản Trị Bằng Mục Tiêu OKR", "John Doerr", 175000.0,
                                "Phương pháp đặt mục tiêu và kết quả then chốt của Google và Intel.", img(i++), kinh));
                books.add(book("Nghệ Thuật Bán Hàng Bậc Cao", "Zig Ziglar", 125000.0,
                                "Kỹ năng và nghệ thuật thuyết phục khách hàng của huyền thoại bán hàng.", img(i++),
                                kinh));
                books.add(book("Marketing 4.0", "Philip Kotler", 185000.0,
                                "Hành trình marketing từ truyền thống đến kỹ thuật số trong thời đại công nghệ.",
                                img(i++), kinh));
                books.add(book("Đòn Tâm Lý Trong Bán Hàng", "Brian Tracy", 110000.0,
                                "Các chiến thuật tâm lý giúp chốt đơn hàng nhanh và hiệu quả hơn.", img(i++), kinh));

                // ─── SỔ TAY - TÂM LÝ (8 cuốn) ────────────────────────────────
                books.add(book("Người Đua Diều", "Khaled Hosseini", 125000.0,
                                "Câu chuyện cảm động về tình bạn, sự phản bội và chuộc lỗi tại Afghanistan.", img(i++),
                                soTay));
                books.add(book("Ký Ức Thuần Khiết", "Dan Brown", 140000.0,
                                "Tiểu thuyết bí ẩn về ký ức, bản sắc và những bí mật chưa được tiết lộ.", img(i++),
                                soTay));
                books.add(book("Sống", "Yu Hua", 98000.0,
                                "Tiểu thuyết Trung Quốc cảm động về ý chí sống mãnh liệt của một người đàn ông.",
                                img(i++), soTay));
                books.add(book("Biết Ơn Từng Ngày", "M.J. Ryan", 80000.0,
                                "Hướng dẫn thực hành lòng biết ơn hàng ngày để tìm hạnh phúc thực sự.", img(i++),
                                soTay));
                books.add(book("Thư Gửi Garcia", "Elbert Hubbard", 55000.0,
                                "Câu chuyện ngắn nổi tiếng về tinh thần trách nhiệm và chủ động trong công việc.",
                                img(i++), soTay));
                books.add(book("Tôi Là Ai và Nếu Vậy Thì Bao Nhiêu?", "Richard David Precht", 120000.0,
                                "Hành trình triết học thú vị khám phá những câu hỏi lớn về ý nghĩa cuộc sống.",
                                img(i++), soTay));
                books.add(book("Cozy Healing: Nắng Xuân Qua Góc Phố", "Nhiều Tác Giả", 89000.0,
                                "Tản văn nhẹ nhàng về những khoảnh khắc bình yên trong cuộc sống đô thị.", img(i++),
                                soTay));
                books.add(book("Nhịp Sống Chợ Lớn", "Nhiều Tác Giả", 95000.0,
                                "Ghi chép về cuộc sống và con người khu Chợ Lớn - Sài Gòn đầy màu sắc.", img(i++),
                                soTay));

                // ─── KHOA HỌC - CÔNG NGHỆ (7 cuốn) ───────────────────────────
                books.add(book("Vũ Trụ Trong Vỏ Hạt Dẻ", "Stephen Hawking", 165000.0,
                                "Khám phá vũ trụ, không-thời gian và lý thuyết dây qua góc nhìn của Hawking.", img(i++),
                                khoaHoc));
                books.add(book("Lược Sử Thời Gian", "Stephen Hawking", 155000.0,
                                "Giải thích Big Bang, lỗ đen và nguồn gốc vũ trụ bằng ngôn ngữ đại chúng.", img(i++),
                                khoaHoc));
                books.add(book("Nhân Tố Enzyme", "Hiromi Shinya", 98000.0,
                                "Bí quyết sức khỏe của bác sĩ người Nhật dựa trên vai trò của enzyme trong cơ thể.",
                                img(i++),
                                khoaHoc));
                books.add(book("Thế Giới Như Tôi Thấy", "Albert Einstein", 120000.0,
                                "Quan điểm và triết lý sống của thiên tài vật lý Albert Einstein.", img(i++), khoaHoc));
                books.add(book("Khoa Học Về Giấc Ngủ", "Matthew Walker", 150000.0,
                                "Nghiên cứu khoa học về tầm quan trọng của giấc ngủ với sức khỏe và não bộ.", img(i++),
                                khoaHoc));
                books.add(book("AI Và Tương Lai Loài Người", "Kai-Fu Lee", 145000.0,
                                "Nhà tiên phong AI phân tích tác động của trí tuệ nhân tạo đến công việc và xã hội.",
                                img(i++),
                                khoaHoc));
                books.add(book("Bộ Não Kỳ Diệu", "John Medina", 130000.0,
                                "12 nguyên tắc khoa học về não bộ giúp bạn học tập và làm việc hiệu quả hơn.", img(i++),
                                khoaHoc));

                bookRepository.saveAll(books);
                log.info("DataSeeder: Saved {} books across {} categories.", books.size(), 10);
        }

        private Book book(String title, String author, Double price, String description, String image,
                        Category category) {
                return Book.builder()
                                .title(title)
                                .author(author)
                                .price(price)
                                .description(description)
                                .image(image)
                                .category(category)
                                .active(true)
                                .build();
        }

        private Category getOrCreate(String name) {
                return categoryRepository.findByNameIgnoreCase(name)
                                .orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));
        }
}
