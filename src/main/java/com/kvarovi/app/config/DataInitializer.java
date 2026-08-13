package com.kvarovi.app.config;

import com.kvarovi.app.entity.Building;
import com.kvarovi.app.entity.Category;
import com.kvarovi.app.entity.Role;
import com.kvarovi.app.entity.User;
import com.kvarovi.app.repository.BuildingRepository;
import com.kvarovi.app.repository.CategoryRepository;
import com.kvarovi.app.repository.RoleRepository;
import com.kvarovi.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BuildingRepository buildingRepository,
                           CategoryRepository categoryRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.buildingRepository = buildingRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            return;
        }

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Role stanarRole = new Role();
        stanarRole.setName("ROLE_STANAR");
        roleRepository.save(stanarRole);

        Building zgradaA = new Building();
        zgradaA.setName("Zgrada A");
        zgradaA.setAddress("Bulevar oslobodjenja 15");
        buildingRepository.save(zgradaA);

        Building zgradaB = new Building();
        zgradaB.setName("Zgrada B");
        zgradaB.setAddress("Cara Dusana 42");
        buildingRepository.save(zgradaB);

        List<String> kategorije = List.of("Vodovod", "Struja", "Lift", "Zajednicke prostorije", "Grejanje");
        for (String naziv : kategorije) {
            Category c = new Category();
            c.setName(naziv);
            categoryRepository.save(c);
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Glavni Administrator");
        admin.setRole(adminRole);
        admin.setBuilding(zgradaA);
        userRepository.save(admin);

        User stanar = new User();
        stanar.setUsername("stanar");
        stanar.setPassword(passwordEncoder.encode("stanar123"));
        stanar.setFullName("Petar Petrovic");
        stanar.setRole(stanarRole);
        stanar.setBuilding(zgradaA);
        userRepository.save(stanar);

        System.out.println(">>> Pocetni podaci ubaceni: admin/admin123, stanar/stanar123");
    }
}