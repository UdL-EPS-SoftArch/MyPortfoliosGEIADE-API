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

// CURRENT DESIGN DECISION:
// We use a single User class with a Role field (CREATOR, ADMIN) instead of
// creating separate subclasses for each role. This simplifies the model,
// reduces boilerplate, and keeps role management centralized.
//
// RATIONALE:
// - Roles currently differ mainly in permissions and a few behaviors (e.g., suspending creators, creating admins).
// - Most attributes and methods are shared, so creating subclasses would add unnecessary complexity.
// - Role-specific behavior is enforced programmatically in controllers or services.
//
// FUTURE EXTENSIBILITY:
// - If roles grow in complexity (e.g., unique attributes, many exclusive methods),
//   we could refactor User into a base class and create subclasses for each role (e.g., AdminUser, CreatorUser).
// - This would encapsulate role-specific logic and allow cleaner polymorphism.


@Entity
@Table(name = "DemoUser") //Avoid collision with system table User
@Data 
@EqualsAndHashCode(callSuper = true)
public class User extends UriEntity<String> implements UserDetails {

	
	
	public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role=Role.CREATOR;
	
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

	@Column(nullable = false)
	private boolean enabled = true;

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
		return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+role.name());
	}
	public User(){

	}
	public User(Role role){
		this.role = role;
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
