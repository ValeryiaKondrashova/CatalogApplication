package com.example.catalog.controllers;

import com.example.catalog.config.GlobalClass;
import com.example.catalog.models.Category;
import com.example.catalog.models.Role;
import com.example.catalog.repository.CategoryRepo;
import com.example.catalog.repository.ProductRepo;
import com.example.catalog.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ProductController {

    @Autowired
    GlobalClass globalClass;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/")  //первая страница, что видит любой пользователь
    public String home(Map<String, Object> model) {
        model.put("title", "Catalog");

        return "index";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("title", "Ваша система");

        return "main";
    }

    @GetMapping("/viewProducts")
    public String viewProducts(Map<String, Object> model){

        Iterable<Product> products = productRepo.findAll();
        model.put("products", products);

        Role roleAdmin = Role.ADMIN;
        Role roleUser = Role.USER;
        Role roleSuperUser = Role.SUPERUSER;

        Set<Role> allRolesUser = globalClass.getUserRole();
        System.out.println("roleAdmin = " + roleAdmin);

        for(int i=0; i<allRolesUser.size(); i++){
            if(allRolesUser.contains(roleAdmin)){
                model.put("roleAdmin",true);
                break;
            }
            if(allRolesUser.contains(roleUser)){
                model.put("roleUser",true);
            }
            if(allRolesUser.contains(roleSuperUser)){
                model.put("roleSuperUser",true);
            }
        }

        model.put("title", "Товары");

        return "viewProducts";
    }

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping("/addProduct")
    public String addProduct(Map<String, Object> model) {

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("category", categories);

        model.put("title", "Добавление товара");

        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct (
            @RequestParam String nameProduct,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String noteCommon,
            @RequestParam String noteSpecial,
            Map<String, Object> model
    ){
        Category category1 = categoryRepo.findCategoryByNameCategory(category);

        Product product = new Product(nameProduct, category1, description, price, noteCommon, noteSpecial);
        productRepo.save(product);

        Iterable<Product> products = productRepo.findAll();
        model.put("products", products);
        model.put("title", "Товар");

        return "viewProducts";
    }

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping("/editProducts")
    public String editProducts(Map<String, Object> model){

        Iterable<Product> products = productRepo.findAll();
        model.put("products", products);

        Set<Role> allRolesUser = globalClass.getUserRole();
        for(int i=0; i<allRolesUser.size(); i++){
            if(allRolesUser.contains(Role.USER)){
                model.put("roleUser",true);
            }
        }

        return "editProducts";
    }

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping("/edit/{id}")
    public String editIdProduct(@PathVariable(value = "id") long  id, Map<String, Object> model){

        Iterable<Category> categories = categoryRepo.findAll();
        model.put("category", categories);

        if(!productRepo.existsById(id)){
            return "editProducts";
        }

        Optional<Product> product = productRepo.findById(id);

        ArrayList<Product> resProduct = new ArrayList<>();
        product.ifPresent(resProduct::add);
        model.put("resProduct", resProduct);

        model.put("title", product.get().getNameProduct());

        return "editIdProduct";
    }

    @PostMapping("/edit/{id}")
    public String editIdProduct(
            @PathVariable(value = "id") long  id,
            @RequestParam String nameProduct,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String noteCommon,
            @RequestParam String noteSpecial,
            Map<String, Object> model
    ){
        Product product = productRepo.findById(id).orElseThrow();

        Category category1 = categoryRepo.findCategoryByNameCategory(category);

        product.setNameProduct(nameProduct);
        product.setCategory(category1);
        product.setDescription(description);
        product.setPrice(price);
        product.setNoteCommon(noteCommon);
        product.setNoteSpecial(noteSpecial);
        productRepo.save(product);

        return "redirect:/editProducts";
    }

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping("/deleteProducts")
    public String deleteProducts(Map<String, Object> model){
        model.put("title", "Удаление товара");

        Set<Role> allRolesUser = globalClass.getUserRole();
        for(int i=0; i<allRolesUser.size(); i++){
            if(allRolesUser.contains(Role.USER)){
                model.put("roleUser",true);
            }
        }

        Iterable<Product> products = productRepo.findAll();
        model.put("products", products);

        return "deleteProducts";
    }

    @PostMapping("/deleteProduct/{id}")
    public String removeIdEmployee(@PathVariable(value = "id") long  id){

        productRepo.deleteById(id);

        return "redirect:/deleteProducts";
    }

    @PostMapping("/filterProduct")
    public String filterUser(@RequestParam String filterProduct, Map<String, Object> model){

        if(filterProduct != null && !filterProduct.isEmpty()){
            Product products = productRepo.findProductByNameProduct(filterProduct);
            model.put("products", products);
        }
        else{
            Iterable<Product> productsAll = productRepo.findAll();
            model.put("products", productsAll);
        }

        Set<Role> allRolesUser = globalClass.getUserRole();
        for(int i=0; i<allRolesUser.size(); i++){
            if(allRolesUser.contains(Role.USER)){
                model.put("roleUser",true);
            }
        }

        return "viewProducts";
    }

}
