package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chitietphieunhap")
public class GoodsReceiptDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mact")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "maphieunhap")
    @ToString.Exclude
    private GoodsReceipt goodsReceipt;

    @ManyToOne
    @JoinColumn(name = "masp")
    @ToString.Exclude
    private Product product;

    @Column(name = "soluong")
    private int quantity;

    @Column(name = "dongia")
    private double unitPrice;
}
