package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "NguoiDung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    @Column(name = "TenNguoiDung", length = 100, nullable = false)
    private String tenNguoiDung;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh = false;

    @Column(name = "DienThoai", length = 15)
    private String dienThoai;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "MatKhau", length = 255)
    private String matKhau;

//    @ManyToOne
//    @JoinColumn(name = "MaQuyen", nullable = false)
//    private Quyen quyen;

    @OneToMany(mappedBy = "nguoiDung")
    private List<DiaChi> diaChis;

    @OneToMany(mappedBy = "nguoiDung")
    private List<GioHang> gioHangs;

    @OneToMany(mappedBy = "nguoiDung")
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nguoiDung")
    private List<PhieuXuat> phieuXuats;
}