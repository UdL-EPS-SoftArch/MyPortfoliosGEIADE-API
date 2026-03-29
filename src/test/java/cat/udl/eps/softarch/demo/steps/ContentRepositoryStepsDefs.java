package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import io.cucumber.java.en.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import cat.udl.eps.softarch.demo.domain.Visibility;

import org.junit.jupiter.api.Assertions;

public class ContentRepositoryStepsDefs {

    private final ContentRepository contentRepository;
    private Long createdContentId;
    private final StepDefs stepDefs;

    public ContentRepositoryStepsDefs(
            ContentRepository contentRepository,
            StepDefs stepDefs) {
        this.contentRepository = contentRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there are no Contents in the system")
    public void there_are_no_Contents_in_the_system() {
        contentRepository.deleteAll();
        Assertions.assertEquals(0, contentRepository.count());
    }

    @Given("^There is a Content with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void thereIsAContentWithNameAndDescription(String name, String description) {
        Content content = new Content();
        content.setName(name);
        content.setDescription(description);
        content.setBody("Sample body");
        content.setCreatedAt(ZonedDateTime.now());
        content.setModifiedAt(ZonedDateTime.now());
        content.setVisibility(Visibility.PUBLIC);

        contentRepository.save(content);
    }

    @When("^I create a new content with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateANewContent(String name, String description) throws Throwable {
        Content content = new Content();
        content.setName(name);
        content.setDescription(description);
        content.setBody("Sample body");
        content.setCreatedAt(ZonedDateTime.now());
        content.setModifiedAt(ZonedDateTime.now());
        content.setVisibility(Visibility.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(content))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
        
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        createdContentId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
    }

    @When("^I delete a Content with name \"([^\"]*)\"")
    public void iDeleteContent(String name) throws Throwable {

        // 1. Buscar el contenido por nombre usando el endpoint que sí funciona
        MvcResult searchResult = stepDefs.mockMvc.perform(
                get("/contents")
                    .param("name", name)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andReturn();

        String response = searchResult.getResponse().getContentAsString();

        // 2. Parsear el JSON HAL
        JsonNode root = stepDefs.mapper.readTree(response);

        JsonNode contents = root.path("_embedded").path("contents");

        if (contents.isMissingNode() || !contents.isArray() || contents.size() == 0) {
            throw new RuntimeException("No content found with name: " + name);
        }

        JsonNode contentNode = contents.get(0);

        // 3. Obtener el ID desde el link "self"
        String selfHref = contentNode.path("_links").path("self").path("href").asText();
        Long idToDelete = Long.valueOf(selfHref.substring(selfHref.lastIndexOf("/") + 1));

        // Guardar el ID para el siguiente step
        createdContentId = idToDelete;

        // 4. Borrar el contenido por ID
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/contents/" + idToDelete)
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    @When("^I create a duplicate Content with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iTryToCreateContentDuplicate(String name, String description) throws Exception {
        Content content = new Content();
        content.setName(name);
        content.setDescription(description);
        content.setBody("Sample body");
        content.setCreatedAt(ZonedDateTime.now());
        content.setModifiedAt(ZonedDateTime.now());
        content.setVisibility(Visibility.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(content))
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    @When("^I create an Empty Name Content with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iTryToCreateContentWithEmptyName(String name, String description) throws Exception {
        Content content = new Content();
        content.setName(name);
        content.setDescription(description);
        content.setBody("Sample body");
        content.setCreatedAt(ZonedDateTime.now());
        content.setModifiedAt(ZonedDateTime.now());
        content.setVisibility(Visibility.PUBLIC);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(content))
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }


    @Then("Content existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertNotNull(createdContentId);
        Assertions.assertTrue(contentRepository.existsById(createdContentId));
    }

    @Then("Content existsById should return false")
    public void exists_by_id_should_return_false() {
        Assertions.assertNotNull(createdContentId);
        Assertions.assertFalse(contentRepository.existsById(createdContentId));
    }

    @Then("The Content creation should fail with status 409")
    public void contentCreationShouldFailWith409() throws Exception {
        stepDefs.result.andExpect(status().isConflict());
    }

    @Then("The Content creation should fail with status 400")
    public void contentCreationShouldFailWith400() throws Exception {
        stepDefs.result.andExpect(status().isBadRequest());
    }

}