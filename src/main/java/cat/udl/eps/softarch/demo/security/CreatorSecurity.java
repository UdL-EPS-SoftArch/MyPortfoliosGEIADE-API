package cat.udl.eps.softarch.demo.security;

import org.springframework.stereotype.Component;

@Component("CreatorSecurity")
public class CreatorSecurity {

    // Retorna true si l'usuari loguejat és el mateix que el del perfil
    public boolean isOwner(String principalUsername, String usernameToEdit) {
        return principalUsername.equals(usernameToEdit);
    }
}