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
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    private String name;

    private String description;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    private Double price;

    @OneToMany(mappedBy = "component")
    @ToString.Exclude
    private List<Order> orders;
    private String photo;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Component(String name, String description, Double price, String photo, Developer developer, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.developer = developer;
        this.category = category;
    }
}
