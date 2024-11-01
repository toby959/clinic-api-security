package com.toby959.api.domain.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String user_key;

//     -- chat --
//    @Enumerated(EnumType.STRING)
//    private ProfileType rol;

    @Override      // origin
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }


//######################################
/*
@Override
@JsonIgnore
public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();

    if (rol != null) {
        switch (rol) {
            case ROL_ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROL_ADMIN"));
                break;
            case ROL_USER:
                authorities.add(new SimpleGrantedAuthority("ROL_USER"));
                break;
            case ROL_INVITED:
                authorities.add(new SimpleGrantedAuthority("ROL_INVITED"));
                break;
            default:
                break;
        }
    }
    return authorities;
}
 */

//######################################

    @Override
    public String getPassword() {
        return user_key;
    }

    @Override
    public String getUsername() {
        return login;
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
