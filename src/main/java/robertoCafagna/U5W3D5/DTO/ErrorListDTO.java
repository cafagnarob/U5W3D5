package robertoCafagna.U5W3D5.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorListDTO(String message,
                           LocalDateTime timestamp, List<String> errorList) {
}
