package cat.udl.eps.softarch.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Role;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/{username}/suspend")
    public ResponseEntity<?> suspendUser(@PathVariable String username) {
        User user = userRepository.findById(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if (user.getRole() != Role.CREATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot suspend an admin");
        }

        user.suspendCreator();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}