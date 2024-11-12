package com.example.hardware.component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Data
public class CatalogPaginationData {
    private String sortingField = "name";
    private int page = 0;
    private int pageSize = 6;
    private int totalPages = 1;
}
