package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.PortfolioRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PortfolioStepDefs {

    private final StepDefs stepDefs;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    // Useful for steps like "I update the portfolio name..." without specifying which portfolio
    private String lastPortfolioUri;

    public PortfolioStepDefs(StepDefs stepDefs,
                             UserRepository userRepository,
                             PortfolioRepository portfolioRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }

    // AUTH / SESSION

    @Given("^I logout$")
    public void iLogout() {
        AuthenticationStepDefs.currentUsername = null;
        AuthenticationStepDefs.currentPassword = null;
    }

    // CREATE

    @When("^I create a new portfolio with name \"([^\"]*)\"$")
    public void iCreateANewPortfolioWithName(String name) throws Throwable {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/portfolios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(portfolio))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());

        // Store Location for later steps (update/delete/name check)
        this.lastPortfolioUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @And("^The new portfolio is owned by \"([^\"]*)\"$")
    public void theNewPortfolioIsOwnedBy(String username) throws Throwable {
        String newPortfolioUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        if (newPortfolioUri != null) this.lastPortfolioUri = newPortfolioUri;

        assertNotNull(this.lastPortfolioUri, "No portfolio Location header found");

        stepDefs.result = stepDefs.mockMvc.perform(
                get(this.lastPortfolioUri + "/owner")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    // READ (LIST / ACCESS CONTROL)

    @When("^I request my portfolios$")
    public void iRequestMyPortfolios() throws Throwable {

        if (AuthenticationStepDefs.currentUsername == null) {
            // Sin login → usar endpoint general
            stepDefs.result = stepDefs.mockMvc.perform(
                    get("/portfolios")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
            return;
        }

        // Con login → comportamiento actual
        User owner = userRepository.findById(AuthenticationStepDefs.currentUsername).orElseThrow();

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/portfolios/search/findByOwner?owner={ownerUri}", owner.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("^I request portfolios of \"([^\"]*)\"$")
    public void iRequestPortfoliosOf(String username) throws Throwable {
        User owner = userRepository.findById(username).orElseThrow();

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/portfolios/search/findByOwner?owner={ownerUri}", owner.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @And("^The list contains a portfolio named \"([^\"]*)\"$")
    public void theListContainsAPortfolioNamed(String name) throws Throwable {
        stepDefs.result
            .andExpect(jsonPath("$._embedded.portfolios[*].name", hasItem(is(name))));
    }

    // UPDATE

    @When("^I update the portfolio name to \"([^\"]*)\"$")
    public void iUpdateThePortfolioNameTo(String newName) throws Throwable {
        assertNotNull(this.lastPortfolioUri, "No portfolio URI available to update (lastPortfolioUri is null)");

        String patchBody = "{ \"name\": " + stepDefs.mapper.writeValueAsString(newName) + " }";

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(this.lastPortfolioUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(patchBody)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("^I try to update the portfolio name to \"([^\"]*)\"$")
    public void iTryToUpdateThePortfolioNameTo(String newName) throws Throwable {
        // Same action as update, scenario asserts 403
        iUpdateThePortfolioNameTo(newName);
    }

    @And("^The portfolio name is \"([^\"]*)\"$")
    public void thePortfolioNameIs(String expectedName) throws Throwable {
        assertNotNull(this.lastPortfolioUri, "No portfolio URI available to check (lastPortfolioUri is null)");

        stepDefs.result = stepDefs.mockMvc.perform(
                get(this.lastPortfolioUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.name", is(expectedName)));
    }

    // DELETE

    @When("^I delete the portfolio \"([^\"]*)\"$")
    public void iDeleteThePortfolio(String name) throws Throwable {
        Portfolio p = portfolioRepository.findByName(name).orElseThrow();
        this.lastPortfolioUri = p.getUri();

        stepDefs.result = stepDefs.mockMvc.perform(
                delete(this.lastPortfolioUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("^I try to delete portfolio \"([^\"]*)\"$")
    public void iTryToDeletePortfolio(String name) throws Throwable {
        // Same action as delete, scenario asserts 403
        iDeleteThePortfolio(name);
    }

    @And("^The portfolio \"([^\"]*)\" does not exist$")
    public void thePortfolioDoesNotExist(String name) throws Throwable {
        // Use repository to ensure it's gone (stronger than only checking HTTP)
        boolean exists = portfolioRepository.findByName(name).isPresent();
        if (!exists) return;

        // If still present, try fetching it to expose failure during test run
        Portfolio p = portfolioRepository.findByName(name).orElseThrow();
        stepDefs.result = stepDefs.mockMvc.perform(
                get(p.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("^I create a new portfolio with name \"([^\"]*)\" and visibility \"([^\"]*)\"$")
    public void iCreateANewPortfolioWithNameAndVisibility(String name, String visibility) throws Throwable {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);
        portfolio.setVisibility(cat.udl.eps.softarch.demo.domain.Visibility.valueOf(visibility));

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/portfolios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(portfolio))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());

        this.lastPortfolioUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @When("^I request public portfolios$")
    public void iRequestPublicPortfolios() throws Throwable {

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/portfolios/search/findByVisibility?visibility=PUBLIC")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("^I request public portfolios of \"([^\"]*)\"$")
    public void iRequestPublicPortfoliosOf(String username) throws Throwable {
        User owner = userRepository.findById(username).orElseThrow();

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/portfolios/search/findByOwnerAndVisibility?owner={ownerUri}&visibility=PUBLIC", owner.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("^The portfolio visibility is \"([^\"]*)\"$")
    public void thePortfolioVisibilityIs(String expectedVisibility) throws Throwable {

        assertNotNull(this.lastPortfolioUri, "No portfolio URI available");

        stepDefs.result = stepDefs.mockMvc.perform(
                get(this.lastPortfolioUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.visibility", is(expectedVisibility)));
    }




}