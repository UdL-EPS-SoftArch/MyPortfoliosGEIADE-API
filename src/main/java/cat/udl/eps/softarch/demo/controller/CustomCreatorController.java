package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RepositoryRestController
public class CustomCreatorController {

    private CreatorRepository creatorRepository;
    public CustomCreatorController(CreatorRepository creatorRepository){
        this.creatorRepository = creatorRepository;
    }

    // PUT creators/username
    @PutMapping("/creators/{username}")
    @PreAuthorize("@creatorSecurity.isOwner(principal.username, #username)")
    public ResponseEntity<Creator> updateCreator(@PathVariable String username,
                                                 @RequestBody Creator updated) {

        Creator creator = creatorRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updated.getProfile() != null) {
            creator.setProfile(updated.getProfile());
        }
        if(updated.getEmail() !=null){
            creator.setEmail(updated.getEmail()); 
        }

        creatorRepository.save(creator);
        return ResponseEntity.ok(creator);
    }
    
    @PostMapping("/creators/{username}/suspend")
    public ResponseEntity<Creator> suspendCreator (@PathVariable String username) {

            Creator creator = creatorRepository.findById(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            creator.suspendCreator();
            creatorRepository.save(creator);
            
            return ResponseEntity.ok(creator);
            

    }
    




    
}
