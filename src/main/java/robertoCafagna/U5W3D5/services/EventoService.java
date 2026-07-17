package robertoCafagna.U5W3D5.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W3D5.DTO.EventoDTO;
import robertoCafagna.U5W3D5.entities.Evento;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.BadRequestException;
import robertoCafagna.U5W3D5.exceptions.NotFoundException;
import robertoCafagna.U5W3D5.repositories.EventoRepository;
import robertoCafagna.U5W3D5.repositories.PrenotazioneRepository;

import java.time.LocalDate;

@Service
@Slf4j
public class EventoService {
    private final EventoRepository eventoRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public EventoService(EventoRepository eventoRepository, PrenotazioneRepository prenotazioneRepository) {
        this.eventoRepository = eventoRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public Evento save(EventoDTO body, User organizzatore) {
        if (body.data().isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Non è possibile creare un viaggio nel passato"
            );
        }

        if (eventoRepository.existsByLuogoAndData(
                body.luogo().trim().toLowerCase(),
                body.data())) {

            throw new BadRequestException(
                    "Esiste già un evento per questo luogo nella stessa data"
            );
        }

        Evento newEvento = new Evento(body.titolo(),
                body.descrizione(),
                body.data(),
                body.luogo(),
                body.disponibilitaPosti(),
                organizzatore);

        Evento saved = this.eventoRepository.save(newEvento);

        log.info("La risorsa " + saved.getId() + " salvato");

        return saved;
    }

    public Page<Evento> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(Long eventoId) {
        return this.eventoRepository.findById(eventoId).orElseThrow(() -> new NotFoundException(eventoId));
    }

    public Evento findByIdAndUpdate(Long eventoId, EventoDTO body) {
        Evento found = this.findById(eventoId);

        if (!found.getOrganizzatore().getId()
                .equals(organizzatore.getId())) {

            throw new BadRequestException(
                    "Non puoi modificare un evento che non hai creato"
            );
        }


        found.setData(body.data());
        found.setTitolo(body.titolo());
        found.setDescrizione(body.descrizione());
        found.setLuogo(body.luogo());
        found.setDiponibilitaPosti(body.disponibilitaPosti());


        Evento updateEvento = this.eventoRepository.save(found);

        return updateEvento;
    }


    public void findByIdAndDelete(Long eventoId, EventoDTO body, User currentUser) {
        Evento found = this.findById(eventoId);
        if (!found.getOrganizzatore().getId()
                .equals(currentUser.getId())) {

            throw new BadRequestException(
                    "Non puoi eliminare un evento che non hai creato"
            );
        }
        if (prenotazioneRepository.existsByEvento_Id(eventoId)) {
            throw new BadRequestException(
                    "Non puoi eliminare un evento con prenotazioni associate"
            );
        }
        this.eventoRepository.delete(found);
    }
}
