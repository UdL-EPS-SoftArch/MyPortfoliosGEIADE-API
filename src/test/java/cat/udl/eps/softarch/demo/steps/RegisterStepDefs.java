package cat.udl.eps.softarch.demo.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.AdminRepository;
import cat.udl.eps.softarch.demo.repository.CreatorRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class RegisterStepDefs {

    private final StepDefs stepDefs;
    private final CreatorRepository creatorRepository;
    private final AdminRepository adminRepository;

    public RegisterStepDefs(StepDefs stepDefs, CreatorRepository creatorRepository, AdminRepository adminRepository) {
        this.stepDefs = stepDefs;
        this.creatorRepository = creatorRepository;
        this.adminRepository = adminRepository;
    }

    // ----------------- GIVEN -----------------
    @Given("^There is no registered creator with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredCreatorWithUsername(String username) {
        assertFalse(creatorRepository.existsById(username), "Creator \"" + username + "\" shouldn't exist");
    }

    @Given("^There is a registered creator with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredCreator(String username, String password, String email) {
        if (!creatorRepository.existsById(username)) {
            Creator creator = new Creator();
            creator.setUsername(username);
            creator.setEmail(email);
            creator.setPassword(password);
            creator.encodePassword();
            creatorRepository.save(creator);
        }
    }

    @Given("^There is a registered admin with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredAdmin(String username, String password, String email) {
        if (!adminRepository.existsById(username)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.encodePassword();
            adminRepository.save(admin);
        }
    }

    // ----------------- WHEN -----------------
    @When("^I register a new creator with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewCreator(String username, String email, String password) throws Exception {
        Creator creator = new Creator();
        creator.setUsername(username);
        creator.setEmail(email);
        creator.setPassword(password);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/creators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(creator))
                                .put("password", password)
                                .toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("^I register a new admin with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewAdmin(String username, String email, String password) throws Exception {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(admin))
                                .put("password", password)
                                .toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // ----------------- THEN / AND -----------------
    @And("^It has been created a creator with username \"([^\"]*)\" and email \"([^\"]*)\", the password is not returned$")
    public void itHasBeenCreatedACreator(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @And("^It has not been created a creator with username \"([^\"]*)\"$")
    public void itHasNotBeenCreatedACreator(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/creators/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isNotFound());
    }
}