package com.example.ecommerceBE.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "size_charts")
@Data
@NoArgsConstructor
public class SizeChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "sizeChart", cascade = CascadeType.ALL)
    private List<SizeChartRow> rows;
}
