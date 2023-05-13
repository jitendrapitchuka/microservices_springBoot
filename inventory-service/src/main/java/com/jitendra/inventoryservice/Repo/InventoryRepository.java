package com.jitendra.inventoryservice.Repo;

import com.jitendra.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    public List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
