package org.prgrms.kyu.repository;

import org.prgrms.kyu.entity.Order;
import org.prgrms.kyu.entity.OrderFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFoodRepository extends JpaRepository<OrderFood, Long> {

}
