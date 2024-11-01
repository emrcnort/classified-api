package com.sahibinden.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahibinden.dto.ClassifiedDto;
import com.sahibinden.enums.Category;
import com.sahibinden.enums.Status;
import com.sahibinden.service.ClassifiedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClassifiedControllerTest {
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @MockBean
    private  ClassifiedService classifiedService;

    @Test
     void testCreateClassified_shouldCreateClassified() throws Exception {
        ClassifiedDto classifiedDto = new ClassifiedDto();
        classifiedDto.setTitle("Title example");
        classifiedDto.setCategory(Category.VASITA);
        classifiedDto.setCreateDate(LocalDateTime.now());
        classifiedDto.setEndDate(classifiedDto.calculateEndDate(Category.VASITA));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dashboard/classifieds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classifiedDto)))
                .andExpect(status().isOk());
    }

    @Test
     void testChangeStatus_shouldChangeClassifiedStatus() throws Exception {
        Long classifiedId = 1L;
        Status newStatus = Status.AKTIF;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/dashboard/classifieds/" + classifiedId + "/status")
                        .param("newStatus", newStatus.toString()))
                .andExpect(status().isOk());
    }

    @Test
     void testGetClassifiedStatistics_shouldGetClassifiedStatistics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dashboard/classifieds/statistics"))
                .andExpect(status().isOk());
    }

    @Test
     void testStatusChanges_shouldGetClassifiedStatusChanges() throws Exception {
        Long classifiedId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dashboard/classifieds/" + classifiedId + "/status-changes"))
                .andExpect(status().isOk());
    }
}
