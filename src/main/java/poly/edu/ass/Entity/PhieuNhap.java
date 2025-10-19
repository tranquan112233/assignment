package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PhieuNhap")
@Data
public class PhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhieuNhap")
    private Integer maPhieuNhap;

    @Column(name = "NgayNhap")
    private LocalDate ngayNhap = LocalDate.now();

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @ManyToOne
    @JoinColumn(name = "MaNCC", nullable = false)
    private NhaCungCap nhaCungCap;

    @OneToMany(mappedBy = "phieuNhap")
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps;
}