package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.domain.Profile;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import cat.udl.eps.softarch.demo.repository.ProfileRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class CreatorEditionProfileDefs {

    private final StepDefs stepDefs;
    private final CreatorRepository creatorRepository;
    private final ProfileRepository profileRepository;

    public CreatorEditionProfileDefs(StepDefs stepDefs, CreatorRepository creatorRepository, ProfileRepository profileRepository ){
        this.creatorRepository = creatorRepository;
        this.profileRepository = profileRepository;
        this.stepDefs = stepDefs;
    }
    
    
    // ----------------------------------------------------
    // Creator has profile
    // ----------------------------------------------------

    @And("creator {string} has profile {string}")
    public void creatorHasProfile(String username, String description) {

        Creator creator = creatorRepository.findById(username).orElseThrow();

        Profile profile = new Profile();
        profile.setDescription(description);

        profileRepository.save(profile);

        creator.setProfile(profile);

        creatorRepository.save(creator);
    }

    // ----------------------------------------------------
    // Creator edits its own profile
    // ----------------------------------------------------

    @When("{string} edits its profile")
    public void creatorEditsItsProfile(String username) throws Exception {

        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = "abcd";

        stepDefs.result = stepDefs.mockMvc.perform(
                put("/creators/" + username + "/profile")
                        .with(AuthenticationStepDefs.authenticate())
        );
    }

    // ----------------------------------------------------
    // Creator edits another creator profile
    // ----------------------------------------------------

    @When("creator {string} edits {string} profile")
    public void creatorEditsOtherProfile(String editor, String owner) throws Exception {

        AuthenticationStepDefs.currentUsername = editor;
        AuthenticationStepDefs.currentPassword = "abcd";

        stepDefs.result = stepDefs.mockMvc.perform(
                put("/creators/" + owner + "/profile")
                        .with(AuthenticationStepDefs.authenticate())
        );
    }
}