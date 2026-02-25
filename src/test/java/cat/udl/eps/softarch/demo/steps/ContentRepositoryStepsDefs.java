package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

public class ContentRepositoryStepsDefs {

    private final ContentRepository contentRepository;
    private boolean existsResult;
    private Long createdContentId;

    public ContentRepositoryStepsDefs(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Given("there are no Contents in the system")
    public void there_are_no_Contents_in_the_system() {
        contentRepository.deleteAll();
        Assertions.assertEquals(0, contentRepository.count());
    }

    @When("I create a Content with name {string} and description {string}")
    public void iCreateAContentWithNameAndDescription(String name, String description) {
        if (!contentRepository.existsByName(name)) {
            Content content = new Content();
            content.setName(name);
            content.setDescription(description);

            Content saved = contentRepository.save(content);
            createdContentId = saved.getContentId();
            existsResult = contentRepository.existsById(createdContentId);
        } else {
            existsResult = true;
        }
    }

    @Then("existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertTrue(existsResult);
    }
}