package org.prgrms.kyu.repository;

import org.prgrms.kyu.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAllByStoreIdOrderById(Long storeId);
}
