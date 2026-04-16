package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.repository.PortfolioRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler(Project.class)
public class ProjectEventHandler {
    private final PortfolioRepository portfolioRepository;

    public ProjectEventHandler(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @HandleBeforeCreate
    public void handleProjectPreCreate(Project project) {
        String username = requireAuthenticatedUsername();
        Portfolio portfolio = requireOwnedPortfolio(project, username);

        ZonedDateTime timeStamp = ZonedDateTime.now();
        project.setPortfolio(portfolio);
        project.setCreated(timeStamp);
        project.setModified(timeStamp);
    }

    @HandleBeforeSave
    public void handleProjectPreSave(Project project) {
        String username = requireAuthenticatedUsername();
        Portfolio portfolio = requireOwnedPortfolio(project, username);

        project.setPortfolio(portfolio);
        project.setModified(ZonedDateTime.now());
    }

    @HandleBeforeDelete
    public void handleProjectPreDelete(Project project) {
        String username = requireAuthenticatedUsername();
        requireOwnedPortfolio(project, username);
    }

    private String requireAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in");
        }

        return auth.getName();
    }

    private Portfolio requireOwnedPortfolio(Project project, String username) {
        if (project.getPortfolio() == null || project.getPortfolio().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project must belong to a portfolio");
        }

        Portfolio portfolio = portfolioRepository.findById(project.getPortfolio().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Portfolio does not exist"));

        if (portfolio.getOwner() == null || !portfolio.getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot manage projects in another user's portfolio");
        }

        return portfolio;
    }
}
