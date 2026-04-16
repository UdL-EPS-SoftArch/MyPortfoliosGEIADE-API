package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
@RepositoryEventHandler(Portfolio.class)
public class PortfolioEventHandler {

    private final UserRepository userRepository;

    public PortfolioEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(Portfolio portfolio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in");
        }

        String username = auth.getName();

        User owner = userRepository.findById(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        portfolio.setOwner(owner);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Portfolio portfolio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in");
        }

        String username = auth.getName();

        if (!portfolio.getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot modify another user's portfolio");
        }
    }

    @HandleBeforeDelete
    public void handleBeforeDelete(Portfolio portfolio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in");
        }

        String username = auth.getName();

        if (!portfolio.getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete another user's portfolio");
        }
    }
}