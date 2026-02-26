package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import cat.udl.eps.softarch.demo.repository.AdminRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

public class UserSuspensionStepDefsMvc {

    private final StepDefs stepDefs;
    private final CreatorRepository creatorRepository;
    private final AdminRepository adminRepository;

    public UserSuspensionStepDefsMvc(StepDefs stepDefs, CreatorRepository creatorRepository, AdminRepository adminRepository) {
        this.stepDefs = stepDefs;
        this.creatorRepository = creatorRepository;
        this.adminRepository = adminRepository;
    }

    // ----------------- Given -----------------
    @Given("there is a registered creator with username {string}, email {string} and password {string}")
    public void thereIsARegisteredCreator(String username, String email, String password) {
        if (!creatorRepository.existsById(username)) {
            Creator creator = new Creator();
            creator.setUsername(username);
            creator.setEmail(email);
            creator.setPassword(password);
            creator.encodePassword();
            creatorRepository.save(creator);
        }
    }

    @Given("There is a registered admin with username {string}, email {string} and password {string}")
    public void thereIsARegisteredAdmin(String username, String email, String password) {
        if (!adminRepository.existsById(username)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.encodePassword();
            adminRepository.save(admin);
        }
    }

    // ----------------- When -----------------
    @When("I suspend the creator {string}")
    public void iSuspendTheCreator(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/creators/{username}/suspend", username)
                        .with(AuthenticationStepDefs.authenticate())
        );
    }

    @When("I attempt to suspend the creator {string}")
    public void iAttemptToSuspendCreator(String username) throws Exception {
        iSuspendTheCreator(username);
    }

    @When("I attempt to suspend the creator {string} as an anonymous user")
    public void iAttemptToSuspendCreatorAnonymous(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/creators/{username}/suspend", username)
        );
    }

    // ----------------- Then -----------------
    @Then("The creator {string} is disabled")
    public void theCreatorIsDisabled(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(jsonPath("$.enabled", is(false)));
    }

    @Then("The creator {string} is still enabled")
    public void theCreatorIsStillEnabled(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(jsonPath("$.enabled", is(true)));
    }
}