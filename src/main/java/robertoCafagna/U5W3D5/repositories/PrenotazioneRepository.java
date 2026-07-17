package robertoCafagna.U5W3D5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W3D5.entities.Prenotazione;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    boolean existsByUtenteIdAndEventoId(Long userId, Long eventoId);

    List<Prenotazione> findByUtenteId(Long id);

    void deleteByUtenteIdAndEventoId(Long userId, Long eventoId);
}
