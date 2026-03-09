package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.domain.Profile;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import io.cucumber.java.en.When;
import net.minidev.json.JSONObject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CreatorEditionStepDefs {

    private final StepDefs stepDefs;

    private final CreatorRepository creatorRepository;

    public CreatorEditionStepDefs(StepDefs stepDefs, CreatorRepository creatorRepository) {
        this.stepDefs = stepDefs;
        this.creatorRepository = creatorRepository;
    }

    //No se si el creo directament a la bd o ho faig amb el mockMvc.perform
    @Given("There is a profile with description {string} for creator {string}")
    public void iCreateMyProfile(String description, String username) throws Throwable {

        Creator creator = creatorRepository.findById(username).orElseThrow();

        Profile profile = new Profile();
        profile.setDescription(description);
        creator.setProfile(profile);

        creatorRepository.save(creator);
        
    }

    @When("I edit my profile with description {string}")
    public void iEditMyProfile(String description) throws Exception {       

        JSONObject body = new JSONObject();
        body.put("description", description);

        stepDefs.result = stepDefs.mockMvc.perform(
                put("/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body.toString())
                    .with(AuthenticationStepDefs.authenticate())
    );
}
}