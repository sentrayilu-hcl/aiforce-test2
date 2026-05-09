package com.company.inventory.service;

import com.company.inventory.model.InventoryItem;
import com.company.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventorySearchServiceTest {

    private InventoryRepository inventoryRepository;
    private InventorySearchService inventorySearchService;

    @BeforeEach
    void setUp() {
        inventoryRepository = Mockito.mock(InventoryRepository.class);
        inventorySearchService = new InventorySearchService(inventoryRepository);
    }

    @Test
    void testSearchByNamePartialCaseInsensitive() {
        InventoryItem i1 = mock(InventoryItem.class);
        when(i1.getName()).thenReturn("Apple iPhone 14");
        when(i1.getCategory()).thenReturn("Electronics");

        InventoryItem i2 = mock(InventoryItem.class);
        when(i2.getName()).thenReturn("IPHONE Case");
        when(i2.getCategory()).thenReturn("Accessories");

        InventoryItem i3 = mock(InventoryItem.class);
        when(i3.getName()).thenReturn("Samsung Galaxy");
        when(i3.getCategory()).thenReturn("Electronics");

        when(inventoryRepository.findAll()).thenReturn(List.of(i1, i2, i3));

        Page<InventoryItem> result = inventorySearchService.search("iphone", null, PageRequest.of(0, 20));

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testSearchByCategoryExactCaseInsensitive() {
        InventoryItem i1 = mock(InventoryItem.class);
        when(i1.getName()).thenReturn("Apple iPhone 14");
        when(i1.getCategory()).thenReturn("Electronics");

        InventoryItem i2 = mock(InventoryItem.class);
        when(i2.getName()).thenReturn("Office Chair");
        when(i2.getCategory()).thenReturn("Furniture");

        InventoryItem i3 = mock(InventoryItem.class);
        when(i3.getName()).thenReturn("USB-C Cable");
        when(i3.getCategory()).thenReturn("electronics");

        when(inventoryRepository.findAll()).thenReturn(List.of(i1, i2, i3));

        Page<InventoryItem> result = inventorySearchService.search(null, "ELECTRONICS", PageRequest.of(0, 20));

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testPagination() {
        List<InventoryItem> items = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            InventoryItem mockItem = mock(InventoryItem.class);
            when(mockItem.getName()).thenReturn("Item " + i);
            when(mockItem.getCategory()).thenReturn("Misc");
            items.add(mockItem);
        }

        when(inventoryRepository.findAll()).thenReturn(items);

        Page<InventoryItem> page1 = inventorySearchService.search(null, null, PageRequest.of(0, 10));
        Page<InventoryItem> page2 = inventorySearchService.search(null, null, PageRequest.of(1, 10));
        Page<InventoryItem> page3 = inventorySearchService.search(null, null, PageRequest.of(2, 10));

        assertEquals(25, page1.getTotalElements());
        assertEquals(3, page1.getTotalPages());
        assertEquals(10, page1.getContent().size());
        assertEquals(10, page2.getContent().size());
        assertEquals(5, page3.getContent().size());
    }
}
