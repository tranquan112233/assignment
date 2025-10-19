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
@Table (name = "DIACHI")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "madc")
    private Integer id;
    @Column (name = "diachi")
    private String address;

    @ManyToOne
    @JoinColumn (name = "mand")
    @ToString.Exclude
    private User user;
}
