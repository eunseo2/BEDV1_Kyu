package org.prgrms.kyu.repository;

import java.util.List;
import org.prgrms.kyu.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findStoresByUserId(Long id);
}
