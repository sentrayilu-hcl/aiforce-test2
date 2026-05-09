package com.company.inventory.controller;

import com.company.inventory.model.InventoryItem;
import com.company.inventory.service.InventorySearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventorySearchController {

    private final InventorySearchService inventorySearchService;

    public InventorySearchController(InventorySearchService inventorySearchService) {
        this.inventorySearchService = inventorySearchService;
    }

    @GetMapping("/search")
    public Page<InventoryItem> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return inventorySearchService.search(name, category, pageable);
    }
}
