package com.company.inventory.service;

import com.company.inventory.model.InventoryItem;
import com.company.inventory.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventorySearchService {

    private final InventoryRepository inventoryRepository;

    public InventorySearchService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<InventoryItem> search(String name, String category, Pageable pageable) {
        List<InventoryItem> all = inventoryRepository.findAll();

        String nameFilter = name == null ? null : name.trim().toLowerCase();
        String categoryFilter = category == null ? null : category.trim().toLowerCase();

        List<InventoryItem> filtered = all.stream()
                .filter(item -> {
                    if (nameFilter == null || nameFilter.isEmpty()) return true;
                    String itemName = item.getName();
                    return itemName != null && itemName.toLowerCase().contains(nameFilter);
                })
                .filter(item -> {
                    if (categoryFilter == null || categoryFilter.isEmpty()) return true;
                    String itemCategory = item.getCategory();
                    return itemCategory != null && itemCategory.equalsIgnoreCase(categoryFilter);
                })
                .collect(Collectors.toList());

        int total = filtered.size();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int fromIndex = Math.min(pageNumber * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<InventoryItem> pageContent = filtered.subList(fromIndex, toIndex);

        return new PageImpl<>(pageContent, pageable, total);
    }
}
