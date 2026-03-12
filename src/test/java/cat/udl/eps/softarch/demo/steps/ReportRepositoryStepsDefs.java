package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.domain.Report;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReportRepositoryStepsDefs {

    private final ReportRepository reportRepository;
    private boolean existsResult;
    private Long createdReportId;
    private final StepDefs stepDefs;
    private final ContentRepository contentRepository;

    @ParameterType(".*") public Content content(String name) {
        return this.contentRepository.findByName(name).orElseThrow(() -> new RuntimeException("Content not found"));
    }

    @ParameterType("\\d+") public Long Long(String value) {
        return Long.valueOf(value);
    }

    public ReportRepositoryStepsDefs(ReportRepository reportRepository, ContentRepository contentRepository, StepDefs stepDefs) {
        this.reportRepository = reportRepository;
        this.contentRepository = contentRepository;
        this.stepDefs = stepDefs;
    }

    @Given("there are no Reports in the system")
    public void there_are_no_Reports_in_the_system() {
        reportRepository.deleteAll();
        Assertions.assertEquals(0, reportRepository.count());
    }

    @When("^I create a Report with reportId (\\d+), contentId (\\d+) and reason \"([^\"]*)\"$")
    public void iCreateAReport(Long reportId, Long contentId, String reason) throws Exception {

        // Construimos el objeto Report a enviar
        Report report = new Report();
        report.setReportId(reportId);

        Content content = new Content();
        content.setContentId(contentId);
        report.setContent(content);

        report.setReason(reason);

        // Llamada a la API
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/reports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(report))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());

        // Obtenemos la respuesta
        String response = stepDefs.result.andReturn().getResponse().getContentAsString();
        Report saved = stepDefs.mapper.readValue(response, Report.class);

        // Guardamos el ID creado para validaciones posteriores
        createdReportId = saved.getReportId();
        existsResult = true; // o podrías hacer un GET para verificar existencia real
    }

    @Then("Report existsById should return true")
    public void exists_by_id_should_return_true() {
        existsResult = contentRepository.existsById(createdReportId);
        Assertions.assertTrue(existsResult);
    }
}