package com.tien.restaurant.service;

import com.tien.restaurant.dto.response.MenuIngredientResponse;
import com.tien.restaurant.entity.MenuIngredient;
import com.tien.restaurant.repository.InventoryItemRepository;
import com.tien.restaurant.repository.MenuIngredientRepository;
import com.tien.restaurant.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuIngredientService {
    private final MenuIngredientRepository menuIngredientRepository;
    private final MenuRepository menuRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public MenuIngredient create(MenuIngredientResponse dto) {
        MenuIngredient mi = new MenuIngredient();
        mi.setMenu(menuRepository.findById(dto.getMenuId()).orElseThrow());
        mi.setInventoryItem(inventoryItemRepository.findById(dto.getInventoryItemId()).orElseThrow());
        mi.setQuantity(dto.getQuantity());
        mi.setUnit(dto.getUnit());
        return menuIngredientRepository.save(mi);
    }

    public MenuIngredient update(Long id, MenuIngredientResponse dto) {
        MenuIngredient mi = menuIngredientRepository.findById(id).orElseThrow();
        mi.setMenu(menuRepository.findById(dto.getMenuId()).orElseThrow());
        mi.setInventoryItem(inventoryItemRepository.findById(dto.getInventoryItemId()).orElseThrow());
        mi.setQuantity(dto.getQuantity());
        mi.setUnit(dto.getUnit());
        return menuIngredientRepository.save(mi);
    }

    public void delete(Long id) {
        menuIngredientRepository.deleteById(id);
    }

    public List<MenuIngredient> getByMenu(Long menuId) {
        return menuIngredientRepository.findByMenuId(menuId);
    }
}
