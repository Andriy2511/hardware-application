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
@ToString
@Entity
@Table(name = "developers")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    private String name;
    private String description;

    @OneToMany (mappedBy = "developer")
    @ToString.Exclude
    private List<Component> components;

    public Developer(String name) {
        this.name = name;
    }

    public Developer(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
