package com.ttknp.api.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "romances")
public class Romance {
    private String rid;
    private String title;
    private Integer price;
}
