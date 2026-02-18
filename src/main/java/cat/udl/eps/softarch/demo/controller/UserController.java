package cat.udl.eps.softarch.demo.controller;


import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cat.udl.eps.softarch.demo.domain.Role;

@RestController
@RequestMapping("/users")
public class UserController{

    private UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/suspend/{id}") //POST users/suspend
    @PreAuthorize("hasRole('ADMIN')")
    public void suspendUser(@PathVariable String id){
        User user_to_be_suspended = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user_to_be_suspended.suspendCreator();
        userRepository.save(user_to_be_suspended);
    }

    @PostMapping("/admins") // POST /users/admins
    @PreAuthorize("hasRole('ADMIN')")
    public void addAdmin(@RequestBody User user){  //Spring demana els parametres del usuari aixi no cal q les inicilizem
        /*
        Logic handled by "@RequestBody User user"
        User user = new User();
        user.setId("admin2");
        user.setEmail("admin2@test.com");
        user.setPassword("12345678");
        */
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        user.encodePassword();
        userRepository.save(user);
    }
}