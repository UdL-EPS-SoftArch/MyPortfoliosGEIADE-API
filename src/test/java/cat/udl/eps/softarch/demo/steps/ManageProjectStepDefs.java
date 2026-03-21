package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.Visibility;
import cat.udl.eps.softarch.demo.repository.ProjectRepository;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ManageProjectStepDefs {

    private final StepDefs stepDefs;
    private final ProjectRepository projectRepository;
    private Project preparedProject;

    public ManageProjectStepDefs(StepDefs stepDefs, ProjectRepository projectRepository) {
        this.stepDefs = stepDefs;
        this.projectRepository = projectRepository;
        this.stepDefs.mapper.registerModule(new JavaTimeModule());
    }

    // ===================== GIVEN =====================

    @Given("I prepare a project with name {string} and description {string} and visibility {string}")
    public void iPrepareAProject(String name, String description, String visibility) {
        preparedProject = new Project();
        preparedProject.setName(name);
        preparedProject.setDescription(description);
        preparedProject.setVisibility(Visibility.valueOf(visibility));
    }

    @And("There is a project with name {string} and description {string} and visibility {string}")
    public void thereIsAProject(String name, String description, String visibility) {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);
        p.setVisibility(Visibility.valueOf(visibility));
        projectRepository.save(p);
    }

    // ===================== WHEN =====================

    @When("I send the create project request")
    public void iSendTheCreateProjectRequest() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(preparedProject))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I retrieve the project named {string}")
    public void iRetrieveTheProjectNamed(String name) throws Exception {
        Project found = projectRepository.findByNameContaining(name)
            .stream().filter(p -> p.getName().equals(name))
            .findFirst().orElseThrow();

        stepDefs.result = stepDefs.mockMvc.perform(
                get("/projects/{id}", found.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I retrieve the project with id {long}")
    public void iRetrieveTheProjectWithId(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/projects/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I update the project named {string} with new name {string}")
    public void iUpdateTheProjectNamed(String oldName, String newName) throws Exception {
        Project found = projectRepository.findByNameContaining(oldName)
            .stream().filter(p -> p.getName().equals(oldName))
            .findFirst().orElseThrow();

        found.setName(newName);

        stepDefs.result = stepDefs.mockMvc.perform(
                put("/projects/{id}", found.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(found))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // ➕ NOU
    @When("I update the project named {string} setting visibility to {string}")
    public void iUpdateTheProjectVisibility(String name, String visibility) throws Exception {
        Project found = projectRepository.findByNameContaining(name)
            .stream().filter(p -> p.getName().equals(name))
            .findFirst().orElseThrow();

        found.setVisibility(Visibility.valueOf(visibility));

        stepDefs.result = stepDefs.mockMvc.perform(
                put("/projects/{id}", found.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(found))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
    
    @When("I retrieve the list of projects")
    public void iRetrieveTheListOfProjects() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/projects")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
}

    @When("I delete the project named {string}")
    public void iDeleteTheProjectNamed(String name) throws Exception {
        Project found = projectRepository.findByNameContaining(name)
            .stream().filter(p -> p.getName().equals(name))
            .findFirst().orElseThrow();

        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/projects/{id}", found.getId())
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I delete the project with id {long}")
    public void iDeleteTheProjectWithId(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/projects/{id}", id)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    // ===================== THEN / AND =====================

    @Then("The project name is {string}")
    public void theProjectNameIs(String name) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @Then("The project creation date should be set")
    public void theProjectCreationDateShouldBeSet() throws Exception {
        stepDefs.result.andExpect(jsonPath("$.created", notNullValue()));
    }

    // ➕ NOU
    @Then("The project modification date should be set")
    public void theProjectModificationDateShouldBeSet() throws Exception {
        stepDefs.result.andExpect(jsonPath("$.modified", notNullValue()));
    }

    @Then("The project list contains a project named {string}")
    public void theProjectListContainsAProjectNamed(String name) throws Exception {
        stepDefs.result.andExpect(
            jsonPath("$._embedded.projects[*].name", hasItem(is(name)))
    );
}

    @Then("The project visibility should be {string}")
    public void theProjectVisibilityShouldBe(String visibility) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.visibility", is(visibility)));
    }
}