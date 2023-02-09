package com.example.catalog.controllers;

import com.example.catalog.models.Role;
import com.example.catalog.models.User;
import com.example.catalog.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserRepo userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/navigationAdmin")
    public String navigationAdmin(Model model) {
        model.addAttribute("title", "Список пользователей");

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users); // в кавычках означает - передаем по имени users

        return "navigationAdmin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/registration")
    public String registration(){

        return "registration";
    }


    @PostMapping ("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb != null){
            model.put("message", "Такой пользователь уже существует!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/navigationAdmin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/editUsers")
    public String editUsers(Model model) {
        model.addAttribute("title", "Редактирование пользователей");

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users); // в кавычках означает - передаем по имени users

        return "editUsers";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roless", Role.values());

        return "userEdit";
    }

    @PostMapping("/user/{user}")
    public String userSave(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Set<Role> roles,
            @RequestParam("userId") User user,
            Map<String, Object> model
    ){

        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);

        userRepository.save(user);

        return "redirect:/navigationAdmin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/removeUsers")
    public String removeUsers(Model model) {
        model.addAttribute("title", "Удаление пользователей");

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "removeUsers";
    }

    @PostMapping("/user/{id}/delete")
    public String userDelete(@PathVariable(value = "id") Long id){

        userRepository.deleteById(id);

        return "redirect:/navigationAdmin";
    }

    @PostMapping("/filterUser")
    public String filterUser(@RequestParam String filterUser, Map<String, Object> model){

        if(filterUser != null && !filterUser.isEmpty()){
            User users = userRepository.findByUsername(filterUser);
            model.put("users", users);
        }
        else{
            Iterable<User> usersAll = userRepository.findAll();
            model.put("users", usersAll);
        }

        return "navigationAdmin";
    }
}