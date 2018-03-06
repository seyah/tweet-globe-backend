package uk.co.seyah.tweetglobebackend.model.graph.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uk.co.seyah.tweetglobebackend.model.graph.relation.UserBias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NodeEntity
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long graphId;

  private String firstName;
  private String lastName;

  private String email;

  private String username;

  @JsonIgnore
  private String password;

  private String token;
  private int role;
  private boolean authenticated;

  @Relationship(type = "UserBias")
  @JsonIgnore
  private List<UserBias> userRatings = new ArrayList<>();

  public User() {
  }

  public User(String username, String token, boolean authenticated) {
    this.username = username;
    this.token = token;
    this.authenticated = authenticated;
  }

  public User(String username, String email, String token, boolean authenticated) {
    this.username = username;
    this.email = email;
    this.token = token;
    this.authenticated = authenticated;
  }

  public Long getGraphId() {
    return graphId;
  }

  public void setGraphId(Long graphId) {
    this.graphId = graphId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

    public String getToken() {
        return token;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public List<UserBias> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(List<UserBias> userRatings) {
        this.userRatings = userRatings;
    }

    @Override
  public String toString() {
    return "User{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", token='" + token + '\'' +
            ", role=" + role +
            ", authenticated=" + authenticated +
            '}';
  }
}