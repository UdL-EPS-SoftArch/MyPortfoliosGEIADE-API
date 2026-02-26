package cat.udl.eps.softarch.demo.controller;
import cat.udl.eps.softarch.demo.repository.ProfileRepository;
import cat.udl.eps.softarch.demo.domain.Profile;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RepositoryRestController("customProfileController")
public class CustomProfileController {

    private final ProfileRepository profileRepository;
    
    public CustomProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }
    
    // PUT /profiles/id --> sobreescriu 
    @PutMapping("/products/{id}")
    @PreAuthorize("@profileSecurity.isOwner(principal.username, #id)")  // per podrela cridar ha de tenir rol CREATOR
    @ResponseBody
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id,
                                                 @RequestBody Profile updatedProfile) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (updatedProfile.getDescription() != null) {
            profile.setDescription(updatedProfile.getDescription());
        }
        if (updatedProfile.getVisibility() != null) {
            profile.setVisibility(updatedProfile.getVisibility());
        }
       
        profileRepository.save(profile);

        return ResponseEntity.ok(profile);
    }

    //Només puc fer GET a un perfil si sóc el propietari o el perfil és públic.
    @GetMapping("/products/{id}")
    @PreAuthorize("@profileSecurity.isOwner(principal.username, #id) or @profileSecurity.isPublic(#id)")
    @ResponseBody
    public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                return ResponseEntity.ok(profile);         
    }

    
    @GetMapping("/products/")
    @ResponseBody
    public ResponseEntity<List<Profile>> getPublicProfiles() {
        List<Profile> publicProfiles = ((List<Profile>) profileRepository.findAll())
                .stream()
                .filter(profile -> profile.getVisibility()==Profile.Visibility.PUBLIC)
                .toList();
        return ResponseEntity.ok(publicProfiles); 
    }
}
