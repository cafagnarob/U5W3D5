package robertoCafagna.U5W3D5.DTO;

import jakarta.validation.constraints.NotNull;

public record PrenotazioneDTO(
        @NotNull(message = "Inserire l'id del dipendente")
        Long userId,
        @NotNull(message = "Inserire l'id del evento")
        Long eventoId) {
}
