package robertoCafagna.U5W3D5.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W3D5.DTO.PrenotazioneNewDTO;
import robertoCafagna.U5W3D5.DTO.PrenotazioneResponseDTO;
import robertoCafagna.U5W3D5.entities.Prenotazione;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.ValidationException;
import robertoCafagna.U5W3D5.services.PrenotazioneService;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    // 1. GET http://localhost:3001/prenotazioni?page=0&size=10&orderBy=dataPrenotazione --> 200 OK
    // Elenco completo di tutte le prenotazioni: solo l'organizzatore deve poterle vedere tutte
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public Page<Prenotazione> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "dataPrenotazione") String orderBy) {
        return this.prenotazioneService.getAll(page, size, orderBy);
    }

    // 2. GET http://localhost:3001/prenotazioni/me --> 200 OK
    // Le prenotazioni dell'utente autenticato
    @GetMapping("/me")
    public List<Prenotazione> getOwnPrenotazioni(@AuthenticationPrincipal User authUser) {
        return this.prenotazioneService.findByUserId(authUser.getId());
    }

    // 3. POST http://localhost:3001/prenotazioni (+req.body) --> 201 CREATED
    // L'utente autenticato crea una prenotazione per sé stesso
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneResponseDTO savePrenotazione(@AuthenticationPrincipal User authUser,
                                                    @Valid @RequestBody PrenotazioneNewDTO body,
                                                    BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Prenotazione saved = this.prenotazioneService.save(authUser.getId(), body.eventoId());
        return new PrenotazioneResponseDTO(saved.getId());
    }

    // 4. DELETE http://localhost:3001/prenotazioni/me/evento/{eventoId} --> 204 NO CONTENT
    // L'utente autenticato cancella la propria prenotazione per un evento
    @DeleteMapping("/me/evento/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnPrenotazione(@AuthenticationPrincipal User authUser,
                                      @PathVariable Long eventoId) {
        this.prenotazioneService.deleteEventoEUser(authUser.getId(), eventoId);
    }

    // 5. GET http://localhost:3001/prenotazioni/{prenotazioneId} --> 200 OK
    // Solo l'organizzatore consulta la singola prenotazione per id
    @GetMapping("/{prenotazioneId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public Prenotazione getById(@PathVariable Long prenotazioneId) {
        return this.prenotazioneService.findById(prenotazioneId);
    }

    // 6. DELETE http://localhost:3001/prenotazioni/{prenotazioneId} --> 204 NO CONTENT
    // L'organizzatore cancella una prenotazione qualsiasi
    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long prenotazioneId) {
        this.prenotazioneService.findByIdAndDelete(prenotazioneId);
    }

    // 7. GET http://localhost:3001/prenotazioni/user/{userId} --> 200 OK
    // L'organizzatore consulta le prenotazioni di un utente specifico
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public List<Prenotazione> findByUser(@PathVariable Long userId) {
        return this.prenotazioneService.findByUserId(userId);
    }

    // 8. DELETE http://localhost:3001/prenotazioni/user/{userId} --> 204 NO CONTENT
    // L'organizzatore cancella tutte le prenotazioni di un utente
    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByUserIdAndDelete(@PathVariable Long userId) {
        this.prenotazioneService.findByUserIdAndDelete(userId);
    }
}