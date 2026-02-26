package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class RegisterStepDefs {

    private final StepDefs stepDefs;
    private final CreatorRepository creatorRepository;

    public RegisterStepDefs(StepDefs stepDefs, CreatorRepository creatorRepository) {
        this.stepDefs = stepDefs;
        this.creatorRepository = creatorRepository;
    }

    // ----------------- GIVEN -----------------
    @Given("^There is no registered creator with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredCreatorWithUsername(String username) {
        creatorRepository.deleteById(username); 
        assertFalse(creatorRepository.existsById(username), "Creator \"" + username + "\" shouldn't exist");
    }

    @Given("^There is a registered creator with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredCreatorWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        if (!creatorRepository.existsById(username)) {
            Creator creator = new Creator();
            creator.setUsername(username);
            creator.setEmail(email);
            creator.setPassword(password);
            creator.encodePassword();
            creatorRepository.save(creator);
        }
    }


    // ----------------- WHEN -----------------
    @When("^I register a new creator with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewCreatorWithUsernameEmailAndPassword(String username, String email, String password) throws Exception {
        Creator creator = new Creator();
        creator.setUsername(username);
        creator.setEmail(email);


        stepDefs.result = stepDefs.mockMvc.perform(
                post("/creators")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject(
                            stepDefs.mapper.writeValueAsString(creator)
                    ).put("password", password).toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // ----------------- THEN / AND -----------------
    @And("^It has been created a creator with username \"([^\"]*)\" and email \"([^\"]*)\", the password is not returned$")
    public void itHasBeenCreatedACreatorWithUsername(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept("application/json")
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print())
         .andExpect(jsonPath("$.email", is(email)))
         .andExpect(jsonPath("$.password").doesNotExist());
    }

    @And("^It has not been created a creator with username \"([^\"]*)\"$")
    public void itHasNotBeenCreatedACreatorWithUsername(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept("application/json")
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(status().isNotFound());
    }

    @And("^I can login with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iCanLoginWithUsernameAndPassword(String username, String password) throws Exception {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/identity")
                        .accept("application/json")
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print())
         .andExpect(status().isOk());
    }

    @And("^I cannot login with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iCannotLoginWithUsernameAndPassword(String username, String password) throws Exception {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/identity")
                        .accept("application/json")
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print())
         .andExpect(status().isUnauthorized());
    }

    @And("There is no creator with username \"([^\"]*)\"")
    public void thereIsNoCreatorWithUsername(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept("application/json")
                        .with(AuthenticationStepDefs.authenticate())
        ).andExpect(status().isNotFound());
    }
}