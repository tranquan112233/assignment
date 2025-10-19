package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PhieuXuat")
@Data
public class PhieuXuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhieuXuat")
    private Integer maPhieuXuat;

    @Column(name = "NgayXuat")
    private LocalDate ngayXuat = LocalDate.now();

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "phieuXuat")
    private List<ChiTietPhieuXuat> chiTietPhieuXuats;
}