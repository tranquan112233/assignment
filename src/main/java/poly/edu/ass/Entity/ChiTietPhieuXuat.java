package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietPhieuXuat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietPhieuXuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCT")
    private Integer maCT;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DonGia", nullable = false)
    private BigDecimal donGia;

    @ManyToOne
    @JoinColumn(name = "MaPhieuXuat", nullable = false)
    private PhieuXuat phieuXuat;

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    private SanPham sanPham;
}