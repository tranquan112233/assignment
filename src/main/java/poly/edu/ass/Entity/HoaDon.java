package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "HoaDon")
@Data
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHoaDon")
    private Integer maHoaDon;

    @Column(name = "NgayLap")
    private LocalDate ngayLap = LocalDate.now();

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Column(name = "TrangThai", length = 50)
    private String trangThai = "Chờ xác nhận";

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaDiaChi", nullable = false)
    private DiaChi diaChi;

    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTiet> hoaDonChiTiets;
}