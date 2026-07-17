package robertoCafagna.U5W3D5.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W3D5.DTO.EventoDTO;
import robertoCafagna.U5W3D5.DTO.EventoResponseDTO;
import robertoCafagna.U5W3D5.entities.Evento;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.ValidationException;
import robertoCafagna.U5W3D5.services.EventoService;

import java.util.List;


@RestController
@RequestMapping("/eventi")
public class EventoController {

    private final EventoService eventoService;


    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }


    // 1. GET
    // http://localhost:3001/eventi?page=0&size=5&orderBy=data
    // --> 200 OK ARRAY DI EVENTI

    @GetMapping
    public Page<Evento> getEventi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "data") String orderBy) {

        return this.eventoService.getAll(page, size, orderBy);
    }


    // 2. POST
    // http://localhost:3001/eventi
    // + req.body
    // --> 201 CREATED ID EVENTO CREATO

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public EventoResponseDTO saveEvento(
            @Valid @RequestBody EventoDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal User currentUser) {


        if (validationResult.hasErrors()) {

            List<String> errorsList = validationResult
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        }


        Evento saved = this.eventoService.save(body, currentUser);

        return new EventoResponseDTO(saved.getId());
    }


    // 3. GET
    // http://localhost:3001/eventi/{eventoId}
    // --> 200 OK EVENTO TROVATO

    @GetMapping("/{eventoId}")
    public Evento getById(
            @PathVariable Long eventoId) {

        return this.eventoService.findById(eventoId);
    }


    // 4. PUT
    // http://localhost:3001/eventi/{eventoId}
    // + payload
    // --> 200 OK EVENTO AGGIORNATO

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public Evento findByIdAndUpdate(
            @PathVariable Long eventoId,
            @Valid @RequestBody EventoDTO body,
            BindingResult validationResult,
            @AuthenticationPrincipal User currentUser) {


        if (validationResult.hasErrors()) {

            List<String> errorsList = validationResult
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        }


        return this.eventoService
                .findByIdAndUpdate(eventoId, body, currentUser);
    }


    // 5. DELETE
    // http://localhost:3001/eventi/{eventoId}
    // --> 204 NO CONTENT

    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(
            @PathVariable Long eventoId,
            @AuthenticationPrincipal User currentUser) {


        this.eventoService
                .findByIdAndDelete(eventoId, currentUser);
    }

}