package com.tien.restaurant.service;

import com.tien.restaurant.dto.request.TutorialRequest;
import com.tien.restaurant.dto.response.TutorialResponse;
import com.tien.restaurant.entity.Tutorial;
import com.tien.restaurant.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialRepository tutorialRepository;
    private final FileStorageService fileStorageService;

    public List<TutorialResponse> getAll() {
        return tutorialRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TutorialResponse create(TutorialRequest request, MultipartFile image) {
        Tutorial tutorial = toEntity(request);
        tutorial.setId(null);

        if (image != null) {
            String imageUrl = fileStorageService.uploadImage(image);
            tutorial.setImage(imageUrl);
        }
        return toDTO(tutorialRepository.save(tutorial));
    }

    public TutorialResponse update(Long id, TutorialRequest request, MultipartFile image) {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutorial not found"));

        // Xóa ảnh cũ nếu có ảnh mới
        if (image != null && tutorial.getImage() != null) {
            fileStorageService.deleteFile(tutorial.getImage());
        }
        tutorial.setTitle(request.getTitle());
        tutorial.setCategory(request.getCategory());
        tutorial.setDescription(request.getDescription());
        tutorial.setIngredients(request.getIngredients());
        tutorial.setSteps(request.getSteps());
        if (image != null) {
            String imageUrl = fileStorageService.uploadImage(image);
            tutorial.setImage(imageUrl);
        }
        return toDTO(tutorialRepository.save(tutorial));
    }

    public void delete(Long id) {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutorial not found"));
        // Xóa ảnh cũ
        if (tutorial.getImage() != null) {
            fileStorageService.deleteFile(tutorial.getImage());
        }
        tutorialRepository.deleteById(id);
    }

    private TutorialResponse toDTO(Tutorial t) {
        return TutorialResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .category(t.getCategory())
                .description(t.getDescription())
                .image(t.getImage())
                .ingredients(t.getIngredients())
                .steps(t.getSteps())
                .build();
    }

    private Tutorial toEntity(TutorialRequest dto) {
        return Tutorial.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .ingredients(dto.getIngredients())
                .steps(dto.getSteps())
                .build();
    }
}
