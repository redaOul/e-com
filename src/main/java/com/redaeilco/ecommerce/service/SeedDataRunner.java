package com.redaeilco.ecommerce.service;

import com.redaeilco.ecommerce.model.Category;
import com.redaeilco.ecommerce.model.Product;
import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.CategoryRepository;
import com.redaeilco.ecommerce.repository.ProductRepository;
import com.redaeilco.ecommerce.repository.UserRepository;
import com.stripe.model.tax.Registration.CountryOptions.Us;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SeedDataRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public void run(String... args) {
        User user = createUser();
        seedCategories(user);
        seedProducts(user);
        linkProductsToCategories();
        System.out.println("Seeding completed successfully!");
    }

    private User createUser() {
        User user = new User("superAdmin", "admin");
        userRepository.save(user);
        return user;
    }

    private void seedCategories(User user) {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                new Category("Electronics", user),
                new Category("Clothing", user),
                new Category("Home & Kitchen", user),
                new Category("Sports & Outdoors", user),
                new Category("Books", user)
            );
            categoryRepository.saveAll(categories);
            System.out.println("Categories seeded.");
        }
    }

    private void seedProducts(User user) {
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                new Product("Laptop", "High-performance laptop", 1200.0, 3, "https://m.media-amazon.com/images/I/51T9X6OH3vL._AC_UF1000,1000_QL80_.jpg", user),
                new Product("Smartphone", "Latest model smartphone", 800.0, 10, "https://media.ldlc.com/r1600/ld/products/00/05/92/69/LD0005926907_1.jpg", user),
                new Product("Headphones", "Noise-canceling headphones", 150.0, 25, "https://imagedelivery.net/4fYuQyy-r8_rpBpcY7lH_A/falabellaPE/137414219_01/w=800,h=800,fit=pad", user),
                new Product("T-Shirt", "Cotton t-shirt", 20.0, 50, "https://www.1083.fr/media/catalog/product/cache/2a03f66ee6f95adf7f510217df4a06fa/3/0/304-x00-00co1xxxxxx_noir___06.jpg", user),
                new Product("Running Shoes", "Comfortable running shoes", 75.0, 30, "https://pyxis.nymag.com/v1/imgs/a6d/fc0/4da4be21d1741718404660586a5b6a6f3e.jpg", user),
                new Product("Cookware Set", "Non-stick cookware set", 200.0, 20, "https://images-cdn.ubuy.ae/64ecff585579160d8d3bec8c-cooks-standard-professional-stainless.jpg", user),
                new Product("Backpack", "Waterproof travel backpack", 50.0, 40, "https://assets.timberland.eu/images/t_img/f_auto,h_650,w_650/e_sharpen:60/dpr_2.0/v1715014395/TB0A61F3EH2-HERO/Timberpack-Backpack-22Litre-in-Dark-Yellow.png", user),
                new Product("Wristwatch", "Stylish analog wristwatch", 100.0, 15, "https://m2.dalvey.com/media/catalog/product/cache/2b036f0fc2b039d5b2b4726d04a72ea9/s/k/skeletal_wrist_watch_sapphirus_navy--03524.jpg", user),
                new Product("Gaming Console", "Next-gen gaming console", 500.0, 12, "https://www.maxgaming.com/bilder/artiklar/liten/28753_S.jpg?m=1700745895", user),
                new Product("Blender", "High-speed blender", 90.0, 22, "https://m.media-amazon.com/images/I/71Bvfzb9P5L.jpg", user),
                new Product("Yoga Mat", "Non-slip yoga mat", 30.0, 35, "https://yolohayoga.com/cdn/shop/files/Aura-Cork-Yoga-Mat.jpg?v=1695911413", user),
                new Product("Wireless Mouse", "Ergonomic wireless mouse", 25.0, 50, "https://www.tradeinn.com/f/13824/138249522/trust-ozaa-wireless-mouse-2400-dpi.webp", user),
                new Product("Desk Lamp", "LED desk lamp", 40.0, 30, "https://i.ebayimg.com/images/g/gf0AAOSwGdBkQQ2Z/s-l1200.png", user),
                new Product("Smartwatch", "Fitness tracking smartwatch", 150.0, 20, "https://media.ldlc.com/r1600/ld/products/00/06/15/12/LD0006151251.jpg", user),
                new Product("Jeans", "Slim fit jeans", 50.0, 40, "https://i5.walmartimages.com/seo/JBL-Flip-5-Portable-Waterproof-Wireless-Bluetooth-Speaker-Ocean-Blue_f48dd24a-724f-4a78-87e0-7ecbd4fa7806.99004274391e08fa1b66610a5623a476.jpeg", user),
                new Product("Jeans", "Slim fit jeans", 40.0, 45, "https://images.blue-tomato.com/is/image/bluetomato/304718022_front.jpg-nC5VzbWx5h1wO9HXqN_HOgxD2jM/304718022+front+jpg.jpg?$tsl$&wid=432&hei=576&fit=crop%2C1", user),
                new Product("Bluetooth Speaker", "Portable Bluetooth speaker", 80.0, 25, "https://i5.walmartimages.com/seo/JBL-Flip-5-Portable-Waterproof-Wireless-Bluetooth-Speaker-Ocean-Blue_f48dd24a-724f-4a78-87e0-7ecbd4fa7806.99004274391e08fa1b66610a5623a476.jpeg", user),
                new Product("Office Chair", "Ergonomic office chair", 250.0, 10, "https://img-us.aosomcdn.com/thumbnail/100/n0/product/2023/12/15/T8v51318c6db54a01.jpg", user),
                new Product("Hiking Boots", "Durable hiking boots", 120.0, 18, "https://www.topoathletic.com/sca-product-images/W054.Olive-Tan_04.jpg?resizeid=11&resizeh=1586&resizew=1586", user),
                new Product("Cookbook", "Delicious recipes cookbook", 25.0, 50, "https://cdn.loveandlemons.com/wp-content/uploads/2023/01/cookbook3.jpg", user),
                new Product("Electric Kettle", "Fast boiling electric kettle", 35.0, 30, "https://i5.walmartimages.com/seo/Variable-Temperature-Electric-Kettle-1200W-Tea-8-Big-Cups-2-0L-Glass-Kettle-4Hrs-Keep-Warm-Function-Boil-Dry-Protection_c9927add-5920-410c-b14a-60204ad01c10.953261aadb73acece3bdb1b6deeee11d.jpeg", user)
            );
            productRepository.saveAll(products);
            System.out.println("Products seeded.");
        }
    }

    private void linkProductsToCategories() {
        List<Map<String, Integer>> productCategories = Arrays.asList(
            Map.of("productId", 1, "categoryId", 1),
            Map.of("productId", 2, "categoryId", 1),
            Map.of("productId", 3, "categoryId", 1),
            Map.of("productId", 4, "categoryId", 2),
            Map.of("productId", 5, "categoryId", 2),
            Map.of("productId", 5, "categoryId", 4),
            Map.of("productId", 6, "categoryId", 3),
            Map.of("productId", 7, "categoryId", 4),
            Map.of("productId", 8, "categoryId", 2),
            Map.of("productId", 9, "categoryId", 1),
            Map.of("productId", 10, "categoryId", 3),
            Map.of("productId", 11, "categoryId", 4),
            Map.of("productId", 12, "categoryId", 1),
            Map.of("productId", 13, "categoryId", 3),
            Map.of("productId", 14, "categoryId", 1),
            Map.of("productId", 15, "categoryId", 2),
            Map.of("productId", 16, "categoryId", 1),
            Map.of("productId", 17, "categoryId", 3),
            Map.of("productId", 18, "categoryId", 2),
            Map.of("productId", 18, "categoryId", 4),
            Map.of("productId", 19, "categoryId", 5),
            Map.of("productId", 20, "categoryId", 3)
        );

        for (Map<String, Integer> pc : productCategories) {
            Optional<Product> productOpt = productRepository.findById(pc.get("productId"));
            Optional<Category> categoryOpt = categoryRepository.findById(pc.get("categoryId"));

            if (productOpt.isPresent() && categoryOpt.isPresent()) {
                Product product = productOpt.get();
                Category category = categoryOpt.get();
                product.getCategories().add(category);
                productRepository.save(product);
            }
        }
        System.out.println("Product-Category relationships established.");
    }
}
