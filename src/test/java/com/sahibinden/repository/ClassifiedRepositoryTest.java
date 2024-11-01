package com.sahibinden.repository;

import com.sahibinden.enums.Category;
import com.sahibinden.enums.Status;
import com.sahibinden.model.Classified;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClassifiedRepositoryTest {
    @Autowired
    private ClassifiedRepository classifiedRepository;
    private Classified classified;

    @BeforeEach
    public void setUp() {
        classified = new Classified();
        classified.setStatus(Status.AKTIF);
        classified.setCategory(Category.VASITA);
        classified.setDetail("Detail example");
        classified.setTitle("Title example");
        classified.setCreateDate(LocalDateTime.now());
        classified.setEndDate(LocalDateTime.now().plusWeeks(3));
        classified= classifiedRepository.save(classified);
    }

    @AfterEach
    public void tearDown() {
        classifiedRepository.delete(classified);
    }

    @Test
    void testFindById_whenMethodCalls_thenReturnClassified() {
        Classified entity = classifiedRepository.findById(classified.getId()).orElse(null);
        assertNotNull(entity);
    }

    @Test
    void whenExistsByCategoryAndTitleAndDetail_thenReturnTrue() {
        boolean exists = classifiedRepository.existsByCategoryAndTitleAndDetail(
                Category.VASITA, "Title example", "Detail example");
        assertTrue(exists);
    }

    @Test
    void whenNotExistsByCategoryAndTitleAndDetail_thenReturnFalse() {
        boolean exists = classifiedRepository.existsByCategoryAndTitleAndDetail(
                Category.EMLAK, "Nonexistent Title", "Nonexistent Detail");
        assertFalse(exists);
    }
}
