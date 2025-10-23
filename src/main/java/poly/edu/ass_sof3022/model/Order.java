package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hoadon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mahoadon")
    private Integer id;

    // Quan hệ ManyToOne với người dùng
    @ManyToOne
    @JoinColumn(name = "manguoidung", nullable = false)
    private User user;

    // Quan hệ ManyToOne với địa chỉ
    @ManyToOne
    @JoinColumn(name = "madiachi", nullable = false)
    private Address address;

    @Column(name = "ngaylap")
    private Date date = getDate();

    @Column(name = "tongtien", precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "trangthai", length = 50)
    private String status = "Chờ xác nhận";

    // Quan hệ OneToMany với chi tiết hóa đơn
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
