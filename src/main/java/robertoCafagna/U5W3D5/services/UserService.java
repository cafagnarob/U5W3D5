package robertoCafagna.U5W3D5.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W3D5.DTO.PasswordChangeDTO;
import robertoCafagna.U5W3D5.DTO.UserDTO;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.BadRequestException;
import robertoCafagna.U5W3D5.exceptions.NotFoundException;
import robertoCafagna.U5W3D5.repositories.PrenotazioneRepository;
import robertoCafagna.U5W3D5.repositories.UserRepository;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public UserService(UserRepository userRepository, PrenotazioneRepository prenotazioneRepository) {
        this.userRepository = userRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public User save(UserDTO body) {
        if (this.userRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'indirizzo email" + body.email() + " è già utilizzato!");
        }
        User newUser = new User(body.name().trim(),
                body.surname().trim(),
                body.email().trim().toLowerCase(),
                body.password());

        User saved = this.userRepository.save(newUser);

        return saved;
    }

    public Page<User> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.userRepository.findAll(pageable);
    }

    public User findById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findAndUpdate(Long userId, UserDTO body) {
        User found = this.findById(userId);

        if (!found.getEmail().equals(body.email()))
            if (this.userRepository.existsByEmail(body.email())) {

                throw new BadRequestException("L'indirizzo email " + body.email() + " è già utilizzato!");
            }
        found.setName(body.name().trim());
        found.setSurname(body.surname().trim());
        found.setEmail(body.email().trim().toLowerCase());

        User updateUser = this.userRepository.save(found);
        return updateUser;
    }

    public void findAndDelete(Long userId) {
        User found = this.findById(userId);
        if (prenotazioneRepository.findByUtenteId(userId) != null) {
            throw new BadRequestException(
                    "Non puoi eliminare un utente con prenotazioni associate"
            );
        }
        this.userRepository.delete(found);
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }

    public void updatePass(long userId, PasswordChangeDTO body) {
        User found = this.findById(userId);
        if (!found.getPassword().equals(body.oldPassword()))
            throw new BadRequestException("Le password non corrispondono!");
        found.setPassword(body.newPassword());
        this.userRepository.save(found);
    }
}
