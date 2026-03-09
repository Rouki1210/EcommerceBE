package com.example.ecommerceBE.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "size_chart_row")
@Data
@NoArgsConstructor
public class SizeChartRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    private Double chest;
    private Double waist;
    private Double length;

    @ManyToOne
    @JoinColumn(name = "size_chart_id")
    private SizeChart sizeChart;
}
