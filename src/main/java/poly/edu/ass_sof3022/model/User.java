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
    @Column (name = "manguoidung")
    private Integer id;
    @Column (name = "tennguoidung")
    private String name;
    @Column (name = "gioitinh")
    private boolean gender;
    @Column (name = "dienthoai")
    private String phonenumber;
    @Column (name = "email")
    private String email;
    @Column (name = "matkhau")
    private String password;

    @ManyToOne
    @JoinColumn(name = "maquyen")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Address> addresses;

    public String getGenderText() {
        return gender ? "Nam" : "Nữ";
    }
}
