package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "nhacungcap")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mancc")
    private Integer id;
    @Column(name = "tenncc")
    private String name;

    private String email;

    @Column(name = "dienthoai")
    private String phone;

    @Column(name = "diachi")
    private String address;

    @OneToMany(mappedBy = "supplier")
    @ToString.Exclude
    List<Product> products;
}
