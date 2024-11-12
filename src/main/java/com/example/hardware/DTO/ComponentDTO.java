package com.example.hardware.DTO;

import com.example.hardware.model.Category;
import com.example.hardware.model.Component;
import com.example.hardware.model.Developer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ComponentDTO {

    private Long id;
    @NotBlank(message = "The name cannot be empty")
    @NotNull(message = "The name cannot be empty")
    private String name;
    private MultipartFile photo;
    private String photoName;

    @Min(value = 0, message = "The price must be higher than 0")
    private Double price;
    private String description;

    private String developer;
    private String category;

    public ComponentDTO(Long id, String name, MultipartFile photo, String description, String developer, Double price, String category){
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.price = price;
        this.developer = developer;
        this.category = category;
    }
}
