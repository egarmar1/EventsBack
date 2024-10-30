package com.kike.events.service.auth;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    public List<String> getRealmRoles(Jwt jwt){
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        return (List<String>) realmAccess.get("roles");
    }

    public String getUserId(Jwt jwt){
        return jwt.getClaim("sub");
    }
}
