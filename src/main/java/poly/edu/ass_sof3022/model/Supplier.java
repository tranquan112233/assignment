package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int id;
    @Column(name = "tenncc")
    private String name;
    @Column(name = "diachi")
    private String address;
    @Column(name = "dienthoai")
    private String phone;

    private String email;

    @OneToMany(mappedBy = "supplier")
    List<Product> products;
}
