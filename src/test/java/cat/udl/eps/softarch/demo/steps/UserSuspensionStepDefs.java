package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import cat.udl.eps.softarch.demo.domain.Role;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UserSuspensionStepDefs {

    private final UserRepository userRepository;
    private Exception lastException;

    public UserSuspensionStepDefs(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //TODO Modificar fent us de StepDefs utilitzant MockMvc per fer les peticions HTTP a l'API en comptes de modificar directament la base de dades. Això permet provar també les capes de seguretat i validació de l'API.
    @Given("There is a registered admin with username {string} and password {string} and email {string}")
    public void thereIsARegisteredAdmin(String username, String password, String email) {
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setId(username);
            user.setEmail(email);
            user.setPassword(password);
            user.encodePassword();
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }

    @When("^I suspend the user \"([^\"]*)\"$")
    public void iSuspendTheUser(String username) {
        User user = userRepository.findById(username).orElseThrow();
        try {
            user.suspendCreator();
            userRepository.save(user);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("^I attempt to suspend the user \"([^\"]*)\"$")
    public void iAttemptToSuspendUser(String username) {
        iSuspendTheUser(username);
    }

    @Then("^The user \"([^\"]*)\" is disabled$")
    public void theUserIsDisabled(String username) {
        User user = userRepository.findById(username).orElseThrow();
        assertFalse(user.isEnabled());
    }

    @Then("^The user \"([^\"]*)\" is still enabled$")
    public void theUserIsStillEnabled(String username) {
        User user = userRepository.findById(username).orElseThrow();
        assertTrue(user.isEnabled());
    }

    @Then("^I get an error \"([^\"]*)\"$")
    public void iGetAnError(String message) {
        assertTrue(lastException.getMessage().contains(message));
    }
}