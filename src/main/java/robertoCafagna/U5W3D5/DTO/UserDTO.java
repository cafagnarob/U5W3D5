package robertoCafagna.U5W3D5.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(@NotBlank(message = "inserire un nome valido")
                      @Size(min = 2, max = 40, message = "Il nome deve avere un numero di caratteri compreso tra 2 e 40")
                      String name,
                      @NotBlank(message = "inserire un cognome valido")
                      @Size(min = 2, max = 40, message = "Il cognome deve avere un numero di caratteri compreso tra 2 e 40")
                      String surname,
                      @NotBlank(message = "inserisci un email valida")
                      @Email(message = "inserisci un email del formato corretto")
                      String email,
                      @NotBlank(message = "inserire una password valida")
                      String password) {
}
