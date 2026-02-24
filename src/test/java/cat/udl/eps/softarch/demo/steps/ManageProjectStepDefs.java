package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ManageProjectStepDefs {
    private Project project;
    private ResponseEntity<Project> response;

    @Autowired
    private TestRestTemplate restTemplate;

    @Given("that I want to create a project with the name {string} and the description {string}")
    public void prepareProject(String name, String desc) {
        project = new Project();
        project.setName(name);
        project.setDescription(desc);
    }

    @When("I send the create request")
    public void sendRequest() {
        response = restTemplate.postForEntity("/projects", project, Project.class);
    }

    @Then("The project should be saved correctly")
    public void verifyIfSaved() {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Then("the data of creation \"created\" should be correct")
    public void verifyCreationDate() {
        Project savedProject = response.getBody();

        assertNotNull(savedProject, "El cos de la resposta no hauria de ser buit");

        assertNotNull(savedProject.getCreated(), "La data de creació s'hauria d'haver generat");

        assertTrue(savedProject.getCreated().isBefore(ZonedDateTime.now().plusSeconds(1)),
            "La data de creació hauria de ser d'ara mateix");
    }
}