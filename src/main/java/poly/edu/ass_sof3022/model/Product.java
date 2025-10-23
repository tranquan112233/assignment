package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sanpham")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "masp")
    private Integer id;
    @Column(name = "tensp")
    private String name;
    @Column(name = "gia")
    private double price;
    @Column(name = "soluong")
    private int quantity;
    @Column(name = "mota")
    private String description;
    @Column(name = "hinhanh")
    private String image;

    @ManyToOne
    @JoinColumn(name = "madanhmuc")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "mancc")
    private Supplier supplier;



}
