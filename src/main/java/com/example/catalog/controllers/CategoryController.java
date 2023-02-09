package com.example.catalog.controllers;

import com.example.catalog.models.Category;
import com.example.catalog.models.Product;
import com.example.catalog.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepo categoryRepo;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/viewCategories")
    public String viewCategories(Map<String, Object> model){

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);

        model.put("title", "Категории");

        return "viewCategories";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/addCategory")
    public String addCategory(Map<String, Object> model) {

        model.put("title", "Добавление категории");

        return "addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategoryPost (
            @RequestParam String nameCategory,
            Map<String, Object> model
    ){
        Category category = new Category(nameCategory);
        categoryRepo.save(category);

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);
        model.put("title", "Категории");

        return "viewCategories";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/editCategories")
    public String editCategories(Map<String, Object> model){

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);

        return "editCategories";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/editCategory/{id}")
    public String editIdCategory(@PathVariable(value = "id") long  id, Map<String, Object> model){

        if(!categoryRepo.existsById(id)){
            return "editCategories";
        }

        Optional<Category> category = categoryRepo.findById(id);

        ArrayList<Category> resCategory = new ArrayList<>();
        category.ifPresent(resCategory::add);
        model.put("resCategory", resCategory);

        model.put("title", category.get().getNameCategory());

        return "editIdCategory";
    }

    @PostMapping("/editCategory/{id}")
    public String editIdCategory(
            @PathVariable(value = "id") long  id,
            @RequestParam String nameCategory,
            Map<String, Object> model
    ){
        Category category = categoryRepo.findById(id).orElseThrow();

        category.setNameCategory(nameCategory);
        categoryRepo.save(category);

        return "redirect:/editCategories";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteCategories")
    public String deleteCategories(Map<String, Object> model){
        model.put("title", "Удаление категории");

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);

        return "deleteCategories";
    }

    @PostMapping("/deleteCategory/{id}")
    public String removeIdCategory(@PathVariable(value = "id") long  id){

        categoryRepo.deleteById(id);

        return "redirect:/deleteCategories";
    }
}
