package com.example.ecommerceBE.entity;

import com.example.ecommerceBE.entity.enums.Badge;
import com.example.ecommerceBE.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(columnList = "sku"),
        @Index(columnList = "category_id"),
        @Index(columnList = "isActive")
})
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Badge badge;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false, unique = true)
    private String sku;

    private String imageUrl;

    private String material;

    private String care;

    private String variant;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> sizes;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private SizeChart sizeChart;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}