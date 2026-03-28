package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TagDeleteStepsDefs {

    private final TagRepository tagRepository;
    private final StepDefs stepDefs;

    private Tag existingTag;

    public TagDeleteStepsDefs(TagRepository tagRepository, StepDefs stepDefs) {
        this.tagRepository = tagRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there is a tag with name {string}")
    public void thereIsATagWithName(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        existingTag = tagRepository.save(tag);
    }

    @When("I delete the tag with name {string}")
    public void iDeleteTheTag(String name) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/tags/" + existingTag.getId())
                        .with(user("admin").roles("ADMIN"))
        ).andDo(print());
    }

    @Then("the tag should not exist in the system")
    public void tagShouldNotExist() {
        assertEquals(0, tagRepository.count());
    }
}