package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "DiaChi")
@Data
@AllArgsConstructor
@NoArgsConstructor
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