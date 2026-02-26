package cat.udl.eps.softarch.demo.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("CREATOR")
@EqualsAndHashCode(callSuper = true)
public class Creator extends User {

    @OneToOne
    @JoinColumn(name = "profile_id", unique = true)
    private Profile profile;

    @Override
    @JsonValue(false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_CREATOR");
    }

    public void suspendCreator() {
        this.enabled = false;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setCreator(this); 
        }
    }

    public Profile getProfile() {
        return profile;
    }
}