package com.tien.restaurant.service;

import com.tien.restaurant.entity.Menu;
import com.tien.restaurant.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll(); // sẽ query trên schema đã được định tuyến
    }

    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }
}
