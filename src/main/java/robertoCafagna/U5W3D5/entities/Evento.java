package robertoCafagna.U5W3D5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String titolo;
    @Setter
    @Column(nullable = false, length = 1000)
    private String descrizione;
    @Setter
    @Column(nullable = false)
    private LocalDate data;
    @Setter
    @Column(nullable = false)
    private String luogo;
    @Setter
    @Column(nullable = false)
    private int diponibilitaPosti;

    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private User organizzatore;


    public Evento(String titolo, String descrizione, LocalDate data,
                  String luogo, int diponibilitaPosti, User organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.diponibilitaPosti = diponibilitaPosti;
        this.organizzatore = organizzatore;
    }

}
