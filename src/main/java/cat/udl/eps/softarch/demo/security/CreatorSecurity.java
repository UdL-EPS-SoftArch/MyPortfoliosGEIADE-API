package cat.udl.eps.softarch.demo.security;

import org.springframework.stereotype.Component;

@Component("CreatorSecurity")
public class CreatorSecurity {

    public boolean isOwner(String principalUsername, String usernameToEdit) {
        return principalUsername.equals(usernameToEdit);
    }
}