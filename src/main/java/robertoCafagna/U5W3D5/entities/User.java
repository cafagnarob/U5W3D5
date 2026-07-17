package robertoCafagna.U5W3D5.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import robertoCafagna.U5W3D5.Enum.Ruolo;

@Entity
@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"password"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String surname;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.ruolo = Ruolo.USER;
    }
}
