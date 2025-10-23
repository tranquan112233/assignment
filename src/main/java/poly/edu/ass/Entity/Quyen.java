package poly.edu.ass.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "quyen")
public class Quyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maQuyen;

    private String tenQuyen;

    @OneToMany(mappedBy = "quyen")
    private List<NguoiDung> nguoiDungs;
}