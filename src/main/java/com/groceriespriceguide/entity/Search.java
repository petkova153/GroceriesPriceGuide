package com.groceriespriceguide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Search {
    private String keyword;
    private List<String> selectedStores;
    private List<String> selectedCategories;
}
