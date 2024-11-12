package com.example.hardware.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    private String name;

    @OneToMany (mappedBy = "category")
    @ToString.Exclude
    List<Component> components;

    public Category(String name) {
        this.name = name;
    }


}
