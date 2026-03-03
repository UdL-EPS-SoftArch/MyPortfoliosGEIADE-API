package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class TagCreateStepsDefs {

    private final TagRepository tagRepository;
    private final StepDefs stepDefs;

    public TagCreateStepsDefs(TagRepository tagRepository, StepDefs stepDefs) {
        this.tagRepository = tagRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there are no tags in the system")
    public void there_are_no_tags_in_the_system() {
        tagRepository.deleteAll();
        Assertions.assertEquals(0, tagRepository.count());
    }

    @When("I create a tag with name {string} and description {string}")
    public void iCreateATagWithNameAndDescription(String name, String description) throws Exception {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(tag))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(user("admin").roles("ADMIN"))
            )
            .andDo(print());
    }

    @Then("Then the tag is created successfully")
    public void the_tag_is_created_successfully() throws Exception {
        stepDefs.result.andExpect(status().isCreated());
    }

    @When("I try to create a tag without a name")
    public void iTryToCreateATagWithoutAName() {
        try {
            Tag tag = new Tag();
            tag.setDescription("No name");
            tagRepository.save(tag);
        } catch (Exception ignored) {
        }
    }
}