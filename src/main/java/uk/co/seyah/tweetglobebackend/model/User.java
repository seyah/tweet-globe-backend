package uk.co.seyah.tweetglobebackend.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class User implements Serializable {

  @Id
  private String id;

  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String password;
  private String token;
  private int role;
  private boolean authenticated;

  public User() {
  }

  public User(String username, String token, boolean authenticated) {
    this.username = username;
    this.token = token;
    this.authenticated = authenticated;
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

  public void setUsername(String username) {
    this.username = username;
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
}