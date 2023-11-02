package com.ilyes.ClientsRegions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;

  @NonNull
  private String firstname;

  @NonNull
  private String lastname;

  @NonNull
  @Column(unique = true)
  private String email;

  @NonNull
  private String password;

  @Getter
  @NonNull
  @Enumerated(EnumType.STRING)
  private Role role;

  @Getter
  private String imagePath;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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
    return true;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }
}
