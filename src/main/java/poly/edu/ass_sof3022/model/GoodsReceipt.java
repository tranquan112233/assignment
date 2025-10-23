package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phieunhap")
public class GoodsReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maphieunhap")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngaynhap")
    private Date date;

    @Column(name = "tongtien")
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "mancc", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "goodsReceipt", cascade = CascadeType.ALL)
    private List<GoodsReceiptDetail> goodsReceiptDetails;
}
