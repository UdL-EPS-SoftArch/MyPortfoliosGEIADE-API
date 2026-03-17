package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import io.cucumber.java.en.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;

public class ContentRepositoryStepsDefs {

    private final ContentRepository contentRepository;
    private boolean existsResult;
    private Long createdContentId;
    private final StepDefs stepDefs;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ContentRepositoryStepsDefs(
            ContentRepository contentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            StepDefs stepDefs) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.stepDefs = stepDefs;
    }

    @Given("there are no Contents in the system")
    public void there_are_no_Contents_in_the_system() {
        contentRepository.deleteAll();
        Assertions.assertEquals(0, contentRepository.count());
    }

    @Given("^There is a registered user with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredUserWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        if (userRepository.findById(username).isEmpty()) {
            User user = new User();
            user.setId(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @When("^I create a new content with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateANewContent(String name, String description) throws Throwable {
        Content content = new Content();
        content.setName(name);
        content.setDescription(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(content))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Then("Content existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertNotNull(createdContentId);
        Assertions.assertTrue(contentRepository.existsById(createdContentId));
    }
}