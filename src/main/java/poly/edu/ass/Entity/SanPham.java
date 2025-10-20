package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "SanPham")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", length = 150, nullable = false)
    private String tenSP;

    @Column(name = "Gia", nullable = false)
    private BigDecimal gia;

    @Column(name = "SoLuong")
    private Integer soLuong = 0;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "hinhanh", length = 255)
    private String hinhAnh;

    @ManyToOne
    @JoinColumn(name = "MaNCC")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "MaDanhMuc")
    private DanhMuc danhMuc;

    @OneToMany(mappedBy = "sanPham")
    private List<GioHang> gioHangs;

    @OneToMany(mappedBy = "sanPham")
    private List<HoaDonChiTiet> hoaDonChiTiets;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietPhieuXuat> chiTietPhieuXuats;
}