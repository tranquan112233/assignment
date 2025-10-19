package poly.edu.ass_sof3022.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "diachi")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "madiachi")
    private Integer id;
    @Column (name = "diachichitiet")
    private String address;
    @Column (name = "macdinh")
    private boolean ísDefault;

    @ManyToOne
    @JoinColumn (name = "manguoidung")
    @ToString.Exclude
    private User user;
}
