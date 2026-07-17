package robertoCafagna.U5W3D5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W3D5.entities.Evento;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByOrganizzatoreId(Long id);
}
