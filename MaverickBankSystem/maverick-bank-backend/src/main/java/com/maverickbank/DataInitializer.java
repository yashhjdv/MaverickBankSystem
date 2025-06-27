package com.maverickbank;

import com.maverickbank.entity.Role;
import com.maverickbank.entity.User;
import com.maverickbank.repository.RoleRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            if (roleRepository.findByName("CUSTOMER").isEmpty())
                roleRepository.save(new Role(null, "CUSTOMER"));
            if (roleRepository.findByName("EMPLOYEE").isEmpty())
                roleRepository.save(new Role(null, "EMPLOYEE"));
            if (roleRepository.findByName("ADMIN").isEmpty())
                roleRepository.save(new Role(null, "ADMIN"));

            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN").get();
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setName("Administrator");
                admin.setEmail("admin@maverickbank.com");
                admin.setContactNumber("0000000000");
                admin.setAddress("HQ");
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);
            }
        };
    }
}
