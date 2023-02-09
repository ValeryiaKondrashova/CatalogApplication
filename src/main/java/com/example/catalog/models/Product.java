package com.example.catalog.models;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameProduct;

    @ManyToOne
    private Category category;

    private String description;
    private double price;
    private String noteCommon;
    private String noteSpecial;

    public Product() {
    }

    public Product(Long id, String nameProduct, Category category, String description, double price, String noteCommon, String noteSpecial) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.category = category;
        this.description = description;
        this.price = price;
        this.noteCommon = noteCommon;
        this.noteSpecial = noteSpecial;
    }

    public Product(String nameProduct, Category category, String description, double price, String noteCommon, String noteSpecial) {
        this.nameProduct = nameProduct;
        this.category = category;
        this.description = description;
        this.price = price;
        this.noteCommon = noteCommon;
        this.noteSpecial = noteSpecial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNoteCommon() {
        return noteCommon;
    }

    public void setNoteCommon(String noteCommon) {
        this.noteCommon = noteCommon;
    }

    public String getNoteSpecial() {
        return noteSpecial;
    }

    public void setNoteSpecial(String noteSpecial) {
        this.noteSpecial = noteSpecial;
    }
}
