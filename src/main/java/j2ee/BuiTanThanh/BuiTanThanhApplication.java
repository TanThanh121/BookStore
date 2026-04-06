package j2ee.BuiTanThanh;

import j2ee.BuiTanThanh.entities.Role;
import j2ee.BuiTanThanh.repositories.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BuiTanThanhApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuiTanThanhApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(IRoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findRoleByName("ADMIN") == null) {
				roleRepository.save(Role.builder().id(1L).name("ADMIN").description("Administrator role").build());
			}
			if (roleRepository.findRoleByName("USER") == null) {
				roleRepository.save(Role.builder().id(2L).name("USER").description("User role").build());
			}
		};
	}
}
