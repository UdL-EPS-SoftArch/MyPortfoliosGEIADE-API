package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagDuplicateStepsDefs {
    private final TagRepository tagRepository;
    private Exception exception;

    public TagDuplicateStepsDefs(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Given("there is already a tag with name {string}")
    public void thereIsAlreadyATag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription("Original");
        tagRepository.save(tag);
    }

    @When("I try to create another tag with name {string} and description {string}")
    public void iTryToCreateAnotherTag(String name, String description) {
        try {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setDescription(description);
            tagRepository.save(tag);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the tag should not be stored in the system")
    public void theTagShouldNotBeStored() {
        long count = tagRepository.count();
        assertEquals(1, count);
    }
}
