package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Role;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

public class UserSuspensionStepDefsMvc {

    private final StepDefs stepDefs;
    private final UserRepository userRepository;

    public UserSuspensionStepDefsMvc(StepDefs stepDefs, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
    }


    @Given("there is a registered creator with username {string}, email {string} and password {string}")
    public void thereIsARegisteredCreator(String username, String email, String password) {
        if (!userRepository.existsById(username)) {
            User user = new User(Role.CREATOR);
            user.setId(username);
            user.setEmail(email);
            user.setPassword(password);
            user.encodePassword();
            userRepository.save(user);
        }
    }

    @Given("There is a registered admin with username {string} and password {string} and email {string}")
    public void thereIsARegisteredAdmin(String username, String password, String email) {
        if (!userRepository.existsById(username)) {
            User user = new User(Role.ADMIN);
            user.setId(username);
            user.setEmail(email);
            user.setPassword(password);
            user.encodePassword();
            userRepository.save(user);
        }
    }


    @When("I suspend the user {string}")
    public void iSuspendTheUser(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/users/{username}/suspend", username)
                        .with(AuthenticationStepDefs.authenticate())
        );
    }

    @When("I attempt to suspend the user {string}")
    public void iAttemptToSuspendUser(String username) throws Exception {
        iSuspendTheUser(username);
    }

    @When("I attempt to suspend the user {string} as an anonymous user")
    public void iAttemptToSuspendUserAnonymous(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/users/{username}/suspend", username)
        );
    }


    @Then("The user {string} is disabled")
    public void theUserIsDisabled(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/users/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(jsonPath("$.enabled", is(false)));
    }

    @Then("The user {string} is still enabled")
    public void theUserIsStillEnabled(String username) throws Exception { // TODO Revisar per fer controller
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/users/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(jsonPath("$.enabled", is(true)));
    }
}