package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.domain.Report;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReportRepositoryStepsDefs {

    private final ReportRepository reportRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            StepDefs stepDefs) {
        this.reportRepository = reportRepository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.stepDefs = stepDefs;
    }

    @Given("there are no Reports in the system")
    public void there_are_no_Reports_in_the_system() {
        reportRepository.deleteAll();
        Assertions.assertEquals(0, reportRepository.count());
    }

    @Given("^There is a registered user with username \"([^\"]*)\" and password \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsARegisteredUserWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        if (userRepository.findById(username).isEmpty()) {
            User user = new User();
            user.setId(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @When("^I create a Report with contentId (\\d+) and reason \"([^\"]*)\"$")
    public void iCreateAReport(Long contentId, String reason) throws Exception {

        Report report = new Report();
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new RuntimeException("Content not found with id " + contentId));

        report.setContent(content);
        report.setReason(reason);

        String response = stepDefs.mockMvc.perform(
                post("/reports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(report))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Report created = stepDefs.mapper.readValue(response, Report.class);
        createdReportId = created.getReportId();
    }

    @Then("Report existsById should return true")
    public void exists_by_id_should_return_true() {
        Assertions.assertNotNull(createdReportId);
        Assertions.assertTrue(reportRepository.existsById(createdReportId));
    }
}