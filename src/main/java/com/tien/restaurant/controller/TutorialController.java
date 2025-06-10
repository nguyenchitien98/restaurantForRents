package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.TutorialRequest;
import com.tien.restaurant.dto.response.TutorialResponse;
import com.tien.restaurant.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tutorials")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping
    public List<TutorialResponse> getAll() {
        return tutorialService.getAll();
    }

    @PostMapping
    public ResponseEntity<TutorialResponse> create(
            @RequestPart("data") TutorialRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {

        return ResponseEntity.ok(tutorialService.create(request, imageFile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorialResponse> update(@PathVariable Long id,
                                   @RequestPart("data") TutorialRequest request,
                                   @RequestPart(value = "imageFile", required = false) MultipartFile image) {
        TutorialResponse updated = tutorialService.update(id, request, image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tutorialService.delete(id);
    }
}