package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;



@Entity
@Table(name = "DemoUser") //Avoid collision with system table User
@Data //Ens proporciona getters i setters
@EqualsAndHashCode(callSuper = true)
public class User extends UriEntity<String> implements UserDetails {

	
	
	public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// TODO Revisar
	// Prevent clients from assigning roles (especially ADMIN) via JSON when creating users.
	// The role field is READ_ONLY in JSON, so it can only be set programmatically in the backend.
	// Admin users can still be created via the /users/admin endpoint, which is protected
	// and accessible only to users with ADMIN role, enforcing proper access control.
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Id
	private String id;

	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank
	@Length(min = 8, max = 256)
	private String password;

	//Falta afegir columna
	private boolean enabled = false;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean passwordReset;

	public void encodePassword() {
		this.password = passwordEncoder.encode(this.password);
	}
	
	@Override
	public String getUsername() { return id; }

	public void setUsername(String username) { this.id = username; }

	@Override
	@JsonValue(value = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Return the user role
		return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+role.name());
	}

	public User(){
		this.enabled = true;
		// Default ROLE
		this.role = Role.CREATOR;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void suspendCreator(){
		if(role == Role.CREATOR){
			this.enabled=false;
		}
		else {
        	throw new IllegalStateException("Only Creators can be suspended");
    	}
	}

}
