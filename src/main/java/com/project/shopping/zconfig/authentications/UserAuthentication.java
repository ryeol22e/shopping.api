package com.project.shopping.zconfig.authentications;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    public UserAuthentication(Object principal, String credentials) {
        super(principal, credentials);
    }

    public UserAuthentication(Object principal, String credentials, List<GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
