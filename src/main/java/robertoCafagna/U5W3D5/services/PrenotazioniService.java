package robertoCafagna.U5W3D5.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W3D5.repositories.PrenotazioneRepository;

@Service
@Slf4j
public class PrenotazioniService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final UserService userService;

    public PrenotazioniService(PrenotazioneRepository prenotazioneRepository, UserService userService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.userService = userService;
    }
}
