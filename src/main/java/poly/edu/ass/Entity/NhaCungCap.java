package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "NhaCungCap")
@Data
public class NhaCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNCC")
    private Integer maNCC;

    @Column(name = "TenNCC", length = 150, nullable = false)
    private String tenNCC;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "DienThoai", length = 15)
    private String dienThoai;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @OneToMany(mappedBy = "nhaCungCap")
    private List<SanPham> sanPhams;

    @OneToMany(mappedBy = "nhaCungCap")
    private List<PhieuNhap> phieuNhaps;
}