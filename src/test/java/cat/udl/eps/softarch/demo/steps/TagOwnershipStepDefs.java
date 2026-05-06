package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;

import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TagOwnershipStepDefs {

    private final StepDefs stepDefs;
    private final TagRepository tagRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    private Tag tag;
    private Content content;

    public TagOwnershipStepDefs(StepDefs stepDefs,
                                TagRepository tagRepository,
                                ContentRepository contentRepository,
                                UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.tagRepository = tagRepository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    @Given("there is a content {string} created by {string}")
    public void thereIsAContentCreatedBy(String contentName, String username) {

        User user = userRepository.findById(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        content = new Content();
        content.setName(contentName);
        content.setUser(user);
        content.setCreatedAt(ZonedDateTime.now());
        content.setModifiedAt(ZonedDateTime.now());
        content.setVisibility(Visibility.PUBLIC);

        content = contentRepository.save(content);
    }

    @When("I assign the tag {string} to content {string}")
    public void iAssignTagToContent(String tagName, String contentName) throws Exception {

        Tag tagEntity = tagRepository.findByName(tagName)
            .orElseThrow(() -> new RuntimeException("Tag not found"));

        Content contentEntity = contentRepository.findById(content.getContentId())
            .orElseThrow(() -> new RuntimeException("Content not found"));

        String authenticatedUsername = "creator1";

        if (!contentEntity.getUser().getUsername().equals(authenticatedUsername)) {
            stepDefs.result = stepDefs.mockMvc.perform(
                    post("/contents/" + contentEntity.getContentId() + "/tags")
                        .contentType("text/uri-list")
                        .content("/tags/" + tagEntity.getId())
                        .with(user(authenticatedUsername))
                ).andDo(print())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isForbidden());
            return;
        }

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/contents/" + contentEntity.getContentId() + "/tags")
                    .contentType("text/uri-list")
                    .content("/tags/" + tagEntity.getId())
                    .with(user(authenticatedUsername))
            ).andDo(print())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNoContent());
    }

    @Then("The response status is {int}")
    public void theResponseStatusIs(int status) throws Exception {
        stepDefs.result.andExpect(
            org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().is(status)
        );
    }

    @Then("content {string} should contain tag {string}")
    @Transactional
    public void contentShouldContainTag(String contentName, String tagName) {

        Content updatedContent = contentRepository.findById(content.getContentId())
            .orElseThrow(() -> new RuntimeException("Content not found"));

        boolean exists = updatedContent.getTags()
            .stream()
            .anyMatch(t -> t.getName().equals(tagName));

        assertTrue(exists, "Tag not assigned correctly to content");
    }
}