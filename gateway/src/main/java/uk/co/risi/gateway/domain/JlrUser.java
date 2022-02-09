package uk.co.risi.gateway.domain;

import uk.co.risi.gateway.domain.json.JlrUserJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class JlrUser implements UserDetails {

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

    public static JlrUser jsonToUser(JlrUserJson dto) {
        var authorities = new HashSet<GrantedAuthority>();
        if (dto.getPermissions() != null)
            for (String dtoAuth : dto.getPermissions())
                authorities.add(new SimpleGrantedAuthority(dtoAuth));
        return new JlrUser(dto.getUsername(), dto.getPassword(), authorities);
    }

    public static JlrUser getDefaultUser() {
        var authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("commands"));
        authorities.add(new SimpleGrantedAuthority("telematics"));
        authorities.add(new SimpleGrantedAuthority("mph"));
        authorities.add(new SimpleGrantedAuthority("1G6DW677X60160257"));
        return new JlrUser("jlrUserAll", "123", authorities);
    }
}
