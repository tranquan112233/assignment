package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nguoidung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    @Column(name = "TenNguoiDung")
    private String tenNguoiDung;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "MatKhau")
    private String matKhau;

    @ManyToOne
    @JoinColumn(name = "MaQuyen")
    private Quyen quyen;

}
