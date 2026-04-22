package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Profile;
import cat.udl.eps.softarch.demo.repository.ProfileRepository;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileStepDefs {

    private final StepDefs stepDefs;
    private final ProfileRepository profileRepository;

    public ProfileStepDefs(StepDefs stepDefs, ProfileRepository profileRepository) {
        this.stepDefs = stepDefs;
        this.profileRepository = profileRepository;
    }

    @When("I create a new profile")
    public void iCreateANewProfile() throws Exception {
        Profile profile = new Profile();
        profile.setDescription("My profile");
        profile.setVisibility(Profile.Visibility.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(profile))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I list public profiles")
    public void iListPublicProfiles() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}

