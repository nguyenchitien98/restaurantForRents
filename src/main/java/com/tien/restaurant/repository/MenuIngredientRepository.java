package com.tien.restaurant.repository;

import com.tien.restaurant.entity.MenuIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuIngredientRepository extends JpaRepository<MenuIngredient, Long> {
    List<MenuIngredient> findByMenuId(Long menuId);
}
