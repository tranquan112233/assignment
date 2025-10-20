package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "DanhMuc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhMuc")
    private Integer maDanhMuc;

    @Column(name = "TenDanhMuc", length = 100, nullable = false)
    private String tenDanhMuc;

    @OneToMany(mappedBy = "danhMuc")
    private List<SanPham> sanPhams;
}