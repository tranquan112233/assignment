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
    private int id;
    @Column(name = "tensp")
    private String name;
    @Column(name = "mota")
    private String description;
    @Column(name = "gia")
    private double price;
    @Column(name = "soluongton")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "maloai")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "mancc")
    private Supplier supplier;



}
