package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TagDuplicateStepsDefs {
    private final TagRepository tagRepository;
    private final StepDefs stepDefs;

    public TagDuplicateStepsDefs(TagRepository tagRepository, StepDefs stepDefs) {
        this.tagRepository = tagRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there is already a tag with name {string}")
    public void thereIsAlreadyATag(String name) throws Exception {
        Tag tag = new Tag();
        tag.setName(name);

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

    @Then("the tag should not be stored in the system")
    public void theTagShouldNotBeStored() {
        long count = tagRepository.count();
        assertEquals(1, count);
    }
}
