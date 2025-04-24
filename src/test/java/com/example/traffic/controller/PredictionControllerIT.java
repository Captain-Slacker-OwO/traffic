package com.example.traffic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class PredictionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_prediction() throws Exception {
        mockMvc.perform(get("/api/predict").param("intersectionId", "123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.prediction").exists());
    }

    @Test
    void should_trigger_fallback() throws Exception {
        // 模拟连续失败触发熔断
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/predict").param("intersectionId", "456"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fallback").value(true));
        }
    }
}