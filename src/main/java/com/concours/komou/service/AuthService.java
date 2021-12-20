package com.concours.komou.service;

import com.concours.komou.app.entity.Response;
import com.concours.komou.entity.ApplicationUser;
import com.concours.komou.entity.UserConnected;
import com.concours.komou.repo.ApplicationUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static com.concours.komou.constants.SecurityConstants.EXPIRATION_TIME;
import static com.concours.komou.constants.SecurityConstants.KEY;


@Service
public class AuthService {
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public ResponseEntity<Map<String, Object>> loginUser(ApplicationUser applicationUser) {

        try {
            ApplicationUser user = applicationUserRepository.findByUsername(applicationUser.getUsername());
            UserConnected userConnected = new UserConnected();

            if (user != null) {

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword()));
                System.out.println("user present " + user.getUsername());
                Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
                Key key = Keys.hmacShaKeyFor(KEY.getBytes());
                Claims claims = Jwts.claims().setSubject(((User) authentication.getPrincipal()).getUsername());
                String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).setExpiration(exp).compact();
                userConnected.setToken(token);
                userConnected.setUsername(applicationUser.getUsername());
                System.out.println("user connected " + userConnected.getUsername());
                return new ResponseEntity<>(Response.success(userConnected, "Authentification réussite"), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new UserConnected(), "Authentification échouée"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new UserConnected(), "Authentification échouée"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
