package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "hoadonchitiet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mact")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mahoadon", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "masp", nullable = false)
    private Product product;

    @Column(name = "soluong", nullable = false)
    private Integer quantity;

    @Column(name = "dongia", precision = 18, scale = 2, nullable = false)
    private BigDecimal price;

    public BigDecimal getThanhTien() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
