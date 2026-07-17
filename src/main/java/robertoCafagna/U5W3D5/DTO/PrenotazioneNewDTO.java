package robertoCafagna.U5W3D5.DTO;

import jakarta.validation.constraints.NotNull;

public record PrenotazioneNewDTO(@NotNull(message = "Inserire l'id del evento")
                                 Long eventoId) {
}
