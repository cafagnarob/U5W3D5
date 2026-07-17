package robertoCafagna.U5W3D5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W3D5.entities.Prenotazione;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    boolean existsByUtente_IdAndEvento_Id(Long userId, Long eventoId);

    ;

    List<Prenotazione> findByUtente_Id(Long id);

    boolean existsByUtente_Id(Long id);

    void deleteByUtente_IdAndEvento_Id(Long userId, Long eventoId);

    boolean existsByEvento_Id(Long eventoid);

}
