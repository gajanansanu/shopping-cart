package com.ecom.shoppingcart.data;

import com.ecom.shoppingcart.model.Product;
import com.ecom.shoppingcart.repo.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void load() {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    new Product(1L, "Pen", BigDecimal.valueOf(10.00)),
                    new Product(2L, "Pencil", BigDecimal.valueOf(5.00)),
                    new Product(3L, "Notebook", BigDecimal.valueOf(45.00)),
                    new Product(4L, "Eraser", BigDecimal.valueOf(3.00)),
                    new Product(5L, "Sharpener", BigDecimal.valueOf(7.00)),
                    new Product(6L, "Scale", BigDecimal.valueOf(12.00)),
                    new Product(7L, "Highlighter", BigDecimal.valueOf(25.00)),
                    new Product(8L, "Marker", BigDecimal.valueOf(20.00)),
                    new Product(9L, "Stapler", BigDecimal.valueOf(60.00)),
                    new Product(10L, "Glue", BigDecimal.valueOf(15.00)),
                    new Product(11L, "A4 Paper Pack", BigDecimal.valueOf(150.00)),
                    new Product(12L, "Whiteboard", BigDecimal.valueOf(500.00)),
                    new Product(13L, "File Folder", BigDecimal.valueOf(18.00)),
                    new Product(14L, "Diary", BigDecimal.valueOf(120.00)),
                    new Product(15L, "Calculator", BigDecimal.valueOf(300.00)),
                    new Product(16L, "Punch Machine", BigDecimal.valueOf(80.00)),
                    new Product(17L, "Sticky Notes", BigDecimal.valueOf(25.00)),
                    new Product(18L, "CD Marker", BigDecimal.valueOf(12.00)),
                    new Product(19L, "Paper Clips", BigDecimal.valueOf(10.00)),
                    new Product(20L, "Binder Clips", BigDecimal.valueOf(15.00)),
                    new Product(21L, "Desk Organizer", BigDecimal.valueOf(250.00)),
                    new Product(22L, "Mouse Pad", BigDecimal.valueOf(100.00)),
                    new Product(23L, "USB Cable", BigDecimal.valueOf(150.00)),
                    new Product(24L, "Pen Stand", BigDecimal.valueOf(40.00)),
                    new Product(25L, "Water Bottle", BigDecimal.valueOf(20.00))
            );
            productRepository.saveAll(products);
        }
    }
}

