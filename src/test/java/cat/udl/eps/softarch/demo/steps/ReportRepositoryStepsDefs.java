package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.domain.Report;

import cat.udl.eps.softarch.demo.repository.ContentRepository;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReportRepositoryStepsDefs {

    private final ReportRepository reportRepository;
    private final ContentRepository contentRepository;
    private final StepDefs stepDefs;
    private Long createdReportId;

    @ParameterType(".*")
    public Content content(String name) {
        return this.contentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Content not found"));
    }

    @ParameterType("\\d+")
    public Long Long(String value) {
        return Long.valueOf(value);
    }

    public ReportRepositoryStepsDefs(
            ReportRepository reportRepository,
            ContentRepository contentRepository,
            StepDefs stepDefs) {
        this.reportRepository = reportRepository;
        this.contentRepository = contentRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there are no Reports in the system")
    public void there_are_no_Reports_in_the_system() {
        reportRepository.deleteAll();
        Assertions.assertEquals(0, reportRepository.count());
    }

    @When("^I create a Report with the last created content and reason \"([^\"]*)\"$")
    public void iCreateAReportWithTheLastCreatedContent(String reason) throws Exception {
        Content content = contentRepository.findAll()
            .stream()
            .reduce((first, second) -> second)
            .orElseThrow(() -> new RuntimeException("No contents found"));

        String contentUri = "/contents/" + content.getContentId();

        com.fasterxml.jackson.databind.node.ObjectNode json = stepDefs.mapper.createObjectNode();
        json.put("reason", reason);
        json.put("createdAt", ZonedDateTime.now().toString());
        json.put("content", contentUri);

        stepDefs.result = stepDefs.mockMvc.perform(
            post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString())
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate()))
        .andDo(print())
        .andExpect(status().isCreated());

        // Leer el ID real desde el header Location
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        createdReportId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
    }

    @Then("Report existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertNotNull(createdReportId);
        Assertions.assertTrue(reportRepository.existsById(createdReportId));
    }

    @When("I delete the last created Report")
    public void iDeleteTheLastCreatedReport() throws Exception {
        Assertions.assertNotNull(createdReportId);

        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/reports/" + createdReportId)
                    .with(AuthenticationStepDefs.authenticate())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    @Then("Report existsById should return false")
    public void reportExistsByIdShouldReturnFalse() {
        Assertions.assertNotNull(createdReportId);
        Assertions.assertFalse(reportRepository.existsById(createdReportId));
    }

    @When("I try to create a duplicate Report with the last created content and reason \"([^\"]*)\"$")
    public void iTryToCreateDuplicateReport(String reason) throws Exception {
        Content content = contentRepository.findAll()
            .stream()
            .reduce((first, second) -> second)
            .orElseThrow(() -> new RuntimeException("No contents found"));

        String contentUri = "/contents/" + content.getContentId();

        com.fasterxml.jackson.databind.node.ObjectNode json = stepDefs.mapper.createObjectNode();
        json.put("reason", reason);
        json.put("createdAt", ZonedDateTime.now().toString());
        json.put("content", contentUri);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/reports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("The Report creation should fail with status 409")
    public void reportCreationShouldFailWith409() throws Exception {
        stepDefs.result.andExpect(status().isConflict());
    }

    @When("I try to create a Report with the last created content and reason \"([^\"]*)\"$")
    public void iTryToCreateReportWithEmptyReason(String reason) throws Exception {
        Content content = contentRepository.findAll()
            .stream()
            .reduce((first, second) -> second)
            .orElseThrow(() -> new RuntimeException("No contents found"));

        String contentUri = "/contents/" + content.getContentId();

        com.fasterxml.jackson.databind.node.ObjectNode json = stepDefs.mapper.createObjectNode();
        json.put("reason", reason);
        json.put("createdAt", ZonedDateTime.now().toString());
        json.put("content", contentUri);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/reports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.toString())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("The Report creation should fail with status 400")
    public void reportCreationShouldFailWith400() throws Exception {
        stepDefs.result.andExpect(status().isBadRequest());
    }
}