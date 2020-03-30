package com.risi.demo.gateway.domain;

import com.risi.demo.gateway.domain.json.TechUserJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class TechUser implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

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

    public static TechUser jsonToUser(TechUserJson dto) {
        var authorities = new HashSet<GrantedAuthority>();
        if (dto.getPermissions() != null)
            for (String dtoAuth : dto.getPermissions())
                authorities.add(new SimpleGrantedAuthority(dtoAuth));
        return new TechUser(dto.getUsername(), dto.getPassword(), authorities);
    }
}
