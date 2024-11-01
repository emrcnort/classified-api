package com.sahibinden.repository;

import com.sahibinden.enums.Category;
import com.sahibinden.model.Classified;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassifiedRepository extends JpaRepository<Classified, Long> {
    boolean existsByCategoryAndTitleAndDetail(Category category, String title, String detail);
}
