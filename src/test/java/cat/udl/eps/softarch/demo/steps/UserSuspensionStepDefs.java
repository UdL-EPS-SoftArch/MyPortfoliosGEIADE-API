package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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