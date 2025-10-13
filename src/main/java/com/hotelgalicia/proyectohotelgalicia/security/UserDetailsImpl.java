package com.hotelgalicia.proyectohotelgalicia.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotelgalicia.proyectohotelgalicia.domain.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class UserDetailsImpl implements UserDetails {
  
  private Long id;

  private String username;
// Ya que verifico por correo, username es el correo (debe llamarse username) mientras que name es el nombre de usuario.
  private String name;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  
  public static UserDetailsImpl build(Usuario user, String nombre) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()));
    return new UserDetailsImpl(
        user.getId(),
        user.getCorreo(),
        nombre,
        user.getContraseña(),
        authorities);
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
  
}
