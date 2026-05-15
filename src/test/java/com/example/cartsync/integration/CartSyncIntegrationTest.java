package com.example.cartsync.integration;

import com.example.cartsync.model.Cart;
import com.example.cartsync.repository.CartSyncRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartSyncIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartSyncRepository cartSyncRepository;

    @MockBean
    private com.example.cartsync.client.InventoryServiceClient inventoryServiceClient;

    @Autowired
    private RedisTemplate<String, Cart> redisTemplate;

    private UUID userId;
    private Cart cart;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        cart = new Cart();
        Cart.CartItem item = new Cart.CartItem();
        item.setProductId("P123");
        item.setQuantity(2);
        cart.setItems(Collections.singletonList(item));

        doNothing().when(inventoryServiceClient).validateInventory(any(Cart.class));
        cartSyncRepository.save(userId, cart);
    }

    @Test
    void testGetCartEndpoint() throws Exception {
        mockMvc.perform(get("/api/cart/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].productId").value("P123"));
    }

    @Test
    void testUpdateCartEndpoint() throws Exception {
        String cartJson = """
            {
              "items": [
                {"productId": "P456", "quantity": 3}
              ]
            }
            """;
        mockMvc.perform(post("/api/cart/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("P456"));
    }

    @Test
    void testSyncCartEndpoint() throws Exception {
        mockMvc.perform(post("/api/cart/sync/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("P123"));
    }

    @Test
    void testReadinessProbe() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}