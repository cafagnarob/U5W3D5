package robertoCafagna.U5W3D5.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventoDTO(
        @NotBlank(message = "inserire un titolo valido")
        @Size(min = 2, max = 40, message = "il titolo deve avere un numero di caratteri compreso tra 2 e 40")
        String titolo,
        @NotBlank(message = "inserire una descrizione valida")
        @Size(min = 2, max = 1000, message = "la descrizione deve avere un numero di caratteri compreso tra 2 e 1000")
        String descrizione,
        @NotNull(message = "inserire una data valida")
        @FutureOrPresent(message = "inserire la data di oggi o una futura")
        LocalDate data,
        @NotBlank(message = "inserire un luogo valido")
        @Size(min = 2, max = 40, message = "il luogo deve avere un numero di caratteri compreso tra 2 e 40")
        String luogo,
        @Positive(message = "i posti devono essere maggiori di 1")
        @Min(value = 1, message = "inserire un numero maggiore di 1")
        int disponibilitaPosti
) {

}
