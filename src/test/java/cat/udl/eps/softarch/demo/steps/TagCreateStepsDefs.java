package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

public class TagCreateStepsDefs {

    private final TagRepository tagRepository;
    private boolean existsResult;
    private Long createdTagId;

    public TagCreateStepsDefs(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Given("there are no tags in the system")
    public void there_are_no_tags_in_the_system() {
        tagRepository.deleteAll();
        Assertions.assertEquals(0, tagRepository.count());
    }

    @When("I create a tag with name {string} and description {string}")
    public void iCreateATagWithNameAndDescription(String name, String description) {
        boolean alreadyExists = tagRepository.findAll()
            .iterator()
            .hasNext() &&
            tagRepository.findAll()
                .iterator()
                .next()
                .getName()
                .equals(name);

        Assertions.assertFalse(alreadyExists, "Tag already exists");
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