package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import cat.udl.eps.softarch.demo.domain.Profile;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
public class CustomCreatorController {

    private CreatorRepository creatorRepository;
    public CustomCreatorController(CreatorRepository creatorRepository){
        this.creatorRepository = creatorRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/creators/{username}/suspend")
    public ResponseEntity<Creator> suspendCreator (@PathVariable String username) {

            Creator creator = creatorRepository.findById(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            creator.suspendCreator();
            creatorRepository.save(creator);
            
            return ResponseEntity.ok(creator);
        
    }

    @PostMapping("/creators")
public ResponseEntity<Creator> createCreator(@RequestBody Creator creator) {
    creator.encodePassword();
    Profile profile = new Profile();
    profile.setDescription("");
    profile.setVisibility(Profile.Visibility.PRIVATE);

    creator.setProfile(profile);

    Creator saved = creatorRepository.save(creator);

    return ResponseEntity.ok(saved);
}

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('CREATOR')")
    public Profile getMyProfile(Authentication auth) {

        Creator creator = creatorRepository.findById(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return creator.getProfile();
    }

   @GetMapping("/creators")
        @PreAuthorize("hasRole('ADMIN')")
        public CollectionModel<Creator> getCreators() {

            List<Creator> creators = new ArrayList<>();
            creatorRepository.findAll().forEach(creators::add);

            return CollectionModel.of(creators,
                linkTo(methodOn(CustomCreatorController.class).getCreators()).withSelfRel()
            );
        }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('CREATOR')")
    public Profile updateMyProfile(@RequestBody Profile updated, Authentication auth) {

    Creator creator = creatorRepository.findById(auth.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Profile profile = creator.getProfile();

    if (profile == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    profile.setDescription(updated.getDescription());
    profile.setVisibility(updated.getVisibility());

    creatorRepository.save(creator);

    return profile;
}
    




    
}
