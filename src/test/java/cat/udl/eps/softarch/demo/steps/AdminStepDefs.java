package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.repository.AdminRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AdminStepDefs {

    private final StepDefs stepDefs;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminStepDefs(StepDefs stepDefs, AdminRepository adminRepository, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Given("^There is no registered user with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredUserWithUsername(String username) {
        userRepository.deleteById(username);
        adminRepository.deleteById(username);
    }

    @Given("^There is no registered admin with username \"([^\"]*)\"$")
    public void thereIsNoRegisteredAdminWithUsername(String username) {
        adminRepository.deleteById(username);
    }

    @Given("^There is a registered admin with username \"([^\"]*)\" and email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void thereIsARegisteredAdminWithUsernameAndEmailAndPassword(String username, String email, String password) {
        if (!adminRepository.existsById(username)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.encodePassword();
            adminRepository.save(admin);
        }
    }

    @Given("^There is a registered admin with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredAdminWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        thereIsARegisteredAdminWithUsernameAndEmailAndPassword(username, email, password);
    }

    @When("^I register a new admin with username \"([^\"]*)\", email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iRegisterANewAdminWithUsernameEmailAndPassword(String username, String email, String password) throws Exception {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(stepDefs.mapper.writeValueAsString(admin)).put("password", password).toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()));
    }
}

