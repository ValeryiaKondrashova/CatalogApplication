package com.example.catalog;

import com.example.catalog.models.Role;
import com.example.catalog.models.User;
import com.example.catalog.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class CatalogApplication implements CommandLineRunner{

    @Autowired
    private UserRepo userRepo;

    public static void main(String[] args)
    {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setUsername("user");
        user.setPassword("user");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");

        User superUser = new User();
        superUser.setUsername("superUser");
        superUser.setPassword("superUser");

        User userFromDb = userRepo.findByUsername(user.getUsername());
        User adminFromDb = userRepo.findByUsername(admin.getUsername());
        User superUserFromDb = userRepo.findByUsername(superUser.getUsername());

        if(userFromDb != null || adminFromDb != null || superUserFromDb != null){
           System.out.println("Такой пользователь уже существует!");
        }
        else{
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);

            admin.setActive(true);
            admin.setRoles(Collections.singleton(Role.ADMIN));
            userRepo.save(admin);

            superUser.setActive(true);
            superUser.setRoles(Collections.singleton(Role.SUPERUSER));
            userRepo.save(superUser);

        }

    }

}
