//package poly.edu.ass.Entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.util.List;
//
//@Entity
//@Table(name = "Quyen")
//@Data

//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//public class Quyen {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaQuyen")
//    private Integer maQuyen;
//
//    @Column(name = "TenQuyen", length = 50, nullable = false)
//    private String tenQuyen;
//
//    @OneToMany(mappedBy = "quyen")
//    private List<NguoiDung> nguoiDungs;
//}