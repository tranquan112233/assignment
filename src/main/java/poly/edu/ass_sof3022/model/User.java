package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "nguoidung")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "mand")
    private int id;
    @Column (name = "hoten")
    private String name;
    @Column (name = "email")
    private String email;
    @Column (name = "matkhau")
    private String password;
    @Column (name = "dienthoai")
    private String phonenumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Address> addresses;
}
