package com.tien.restaurant.controller;

import com.tien.restaurant.entity.Menu;
import com.tien.restaurant.service.MenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @Cacheable("menu") // Sử dụng Caffeine cache để Cache dữ liệu đọc nhiều
    public List<Menu> getMenus() {
        return menuService.getAllMenus();
    }

    @PostMapping
    public Menu createMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }
}
