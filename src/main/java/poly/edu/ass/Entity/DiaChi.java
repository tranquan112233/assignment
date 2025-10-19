package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "DiaChi")
@Data
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDiaChi")
    private Integer maDiaChi;

    @Column(name = "DiaChiChiTiet", length = 255, nullable = false)
    private String diaChiChiTiet;

    @Column(name = "MacDinh")
    private Boolean macDinh = false;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", nullable = false)
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "diaChi")
    private List<HoaDon> hoaDons;
}