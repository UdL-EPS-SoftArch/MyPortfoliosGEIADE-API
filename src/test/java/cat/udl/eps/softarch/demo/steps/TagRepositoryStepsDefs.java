package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

public class TagRepositoryStepsDefs {

    private final TagRepository tagRepository;
    private boolean existsResult;
    private Long createdTagId;

    public TagRepositoryStepsDefs(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Given("there are no tags in the system")
    public void there_are_no_tags_in_the_system() {
        tagRepository.deleteAll();
    }

    @When("I create a tag with name {string} and description {string}")
    public void iCreateATagWithNameAndDescription(String name, String description) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);

        Tag saved = tagRepository.save(tag);
        createdTagId = saved.getId();

        existsResult = tagRepository.existsById(createdTagId);
    }

    @Then("existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertTrue(existsResult);
    }
}