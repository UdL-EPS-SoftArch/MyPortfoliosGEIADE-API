package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Report;
import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

public class ReportRepositoryStepsDefs {

    private final ReportRepository reportRepository;
    private boolean existsResult;
    private Long createdReportId;

    public ReportRepositoryStepsDefs(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Given("there are no Reports in the system")
    public void there_are_no_Reports_in_the_system() {
        reportRepository.deleteAll();
        Assertions.assertEquals(0, reportRepository.count());
    }

    @When("I create a Report with reportId {Long}, content {content} and reason {string}")
    public void iCreateAReportWithNameAndDescription(Long reportId, Content content, String reason) {
        Report report = new Report();
        report.setContent(content);
        report.setReason(reason);

        Report saved = reportRepository.save(report);
        createdReportId = saved.getReportId();
        existsResult = reportRepository.existsById(createdReportId);

    }

    @Then("Report existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertTrue(existsResult);
    }
}