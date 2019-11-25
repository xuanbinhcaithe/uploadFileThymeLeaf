package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.CategoryService;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    Environment env;

    @ModelAttribute("categories")
    public Iterable<Category> viewCategory() {
        Iterable<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping("/create-product")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/create-product")
    public ModelAndView createProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/product/create");
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Product product = new Product(productForm.getName(), fileName, productForm.getPrice(), productForm.getAmount(), productForm.getDescription(), productForm.getCreateDate(), productForm.getCategory());
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", new ProductForm());
        modelAndView.addObject("ms", "Create Product successfully!");
        return modelAndView;
    }
    @GetMapping("/products")
    public ModelAndView listProduct(@RequestParam("s")Optional<String> s) {
        Iterable<Product> products;
        if (s.isPresent()) {
            products = productService.findAllByNameContaining(s.get());
        }else {
            products = productService.findAll();
        }
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/{id}")
    public ModelAndView listProductWithCategory(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        Iterable<Product> products = productService.findAllByCategory(category);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products",products);
        return modelAndView;

    }
    @GetMapping("/products/a")
    public ModelAndView listProductWithPrice1() {
        Iterable<Product> products = productService.findAllByPriceBetween(0.1,10.0);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products",products);
        return modelAndView;
    }

    @GetMapping("/products/b")
    public ModelAndView listProductWithPrice2() {
        Iterable<Product> products = productService.findAllByPriceBetween(10.1,19.9);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products",products);
        return modelAndView;
    }

    @GetMapping("/products/c")
    public ModelAndView listProductWithPrice3() {
        Iterable<Product> products = productService.findAllByPriceBetween(10.1,19.9);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products",products);
        return modelAndView;
    }
    @GetMapping("/edit-product/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ModelAndView("/product/error");
        }
        ProductForm productForm = new ProductForm(product.getId(), product.getName(), null, product.getPrice(), product.getAmount(), product.getDescription(), product.getCreateDate(), product.getCategory());
        ModelAndView modelAndView = new ModelAndView("product/edit");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/edit-product")
    public ModelAndView updateProduct(@ModelAttribute ProductForm productForm, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/product/edit");
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), fileName, productForm.getPrice(), productForm.getAmount(), productForm.getDescription(), productForm.getCreateDate(), productForm.getCategory());
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("product", product);
        modelAndView.addObject("ms", "Create successfully!");
        return modelAndView;
    }

    @GetMapping("/delete-product/{id}")
    public ModelAndView showDeleteForm(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
       ModelAndView modelAndView = new ModelAndView("/product/delete");
       modelAndView.addObject("product",product);
       return modelAndView;
    }
    @PostMapping("/delete-product")
    public String deleteProduct(@ModelAttribute Product product) {
        productService.remove(product.getId());
        return "redirect:products";
    }
}
