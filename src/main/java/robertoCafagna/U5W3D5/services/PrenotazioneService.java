package robertoCafagna.U5W3D5.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import robertoCafagna.U5W3D5.entities.Evento;
import robertoCafagna.U5W3D5.entities.Prenotazione;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.BadRequestException;
import robertoCafagna.U5W3D5.exceptions.NotFoundException;
import robertoCafagna.U5W3D5.repositories.EventoRepository;
import robertoCafagna.U5W3D5.repositories.PrenotazioneRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final UserService userService;
    private final EventoService eventoService;
    private final EventoRepository eventoRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository,
                               UserService userService,
                               EventoService eventoService, EventoRepository eventoRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.userService = userService;
        this.eventoService = eventoService;
        this.eventoRepository = eventoRepository;
    }


    public Prenotazione save(Long userId, Long eventoId) {
        User dFromDB = this.userService.findById(userId);
        Evento eFromDB = this.eventoService.findById(eventoId);


        if (prenotazioneRepository.existsByUtente_IdAndEvento_Id(
                userId, eventoId
        )) {
            throw new BadRequestException(
                    "l'utente " + dFromDB.getName() + dFromDB.getSurname() +
                            " è già registrato all'evento"
            );
        }

        if (eFromDB.getDisponibilitaPosti() <= 0) {
            throw new BadRequestException(
                    "Non ci sono più posti disponibili"
            );
        }
        Prenotazione newPrenotazione = new Prenotazione(dFromDB, eFromDB);

        eFromDB.setDisponibilitaPosti(
                eFromDB.getDisponibilitaPosti() - 1
        );

        eventoRepository.save(eFromDB);

        Prenotazione saved = this.prenotazioneRepository.save(newPrenotazione);


        log.info("La risorsa " + saved.getId() + " salvato");

        return saved;
    }

    public Page<Prenotazione> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(Long prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() ->
                new NotFoundException(prenotazioneId));
    }

    @Transactional
    public void findByIdAndDelete(Long prenotazioneId) {
        Prenotazione found = this.findById(prenotazioneId);

        if (found.getEvento().getData()
                .isBefore(LocalDate.now())) {

            throw new BadRequestException(
                    "Non puoi eliminare una prenotazione per un viaggio già passato"
            );
        }

        Evento evento = found.getEvento();
        evento.setDisponibilitaPosti(evento.getDisponibilitaPosti() + 1);
        eventoRepository.save(evento);


        this.prenotazioneRepository.delete(found);
    }


    public List<Prenotazione> findByUserId(Long userId) {
        List<Prenotazione> found = this.prenotazioneRepository.findByUtente_Id(userId);
        if (found.isEmpty()) {
            throw new NotFoundException(userId);
        }
        found.forEach(System.out::println);
        return found;
    }

    @Transactional
    public void findByUserIdAndDelete(Long userId) {
        List<Prenotazione> found = this.findByUserId(userId);
        found.forEach(p -> {
            Evento evento = p.getEvento();
            evento.setDisponibilitaPosti(evento.getDisponibilitaPosti() + 1);
            eventoRepository.save(evento);
            prenotazioneRepository.delete(p);
        });
        log.info("tutte le prenotazioni dell'Utente " + userId + "sono stati eliminati");
    }

    @Transactional
    public void deleteEventoEUser(Long userId, Long eventoId) {
        if (!prenotazioneRepository.existsByUtente_IdAndEvento_Id(userId, eventoId)) {
            throw new NotFoundException(
                    "Non esiste una prenotazione per questo utente e questo evento"
            );
        }

        Evento evento = eventoService.findById(eventoId);
        evento.setDisponibilitaPosti(evento.getDisponibilitaPosti() + 1);
        eventoRepository.save(evento);

        prenotazioneRepository.deleteByUtente_IdAndEvento_Id(userId, eventoId);
    }
}
