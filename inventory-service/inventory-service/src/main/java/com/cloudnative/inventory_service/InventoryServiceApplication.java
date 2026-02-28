package com.cloudnative.inventory_service;

import com.cloudnative.inventory_service.model.Inventory;
import com.cloudnative.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository){
        return args -> {
            if(inventoryRepository.findAll().isEmpty()) {
                Inventory iphone = new Inventory();
                iphone.setSkuCode("IPHONE-15-PRO");
                iphone.setQuantity(100);
                inventoryRepository.save(iphone);
                System.out.println("On inventory added 100 IPHONE-15-PRO");
            }
        };
    }

}
