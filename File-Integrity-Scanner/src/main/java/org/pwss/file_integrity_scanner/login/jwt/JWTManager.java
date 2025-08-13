package org.pwss.file_integrity_scanner.login.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;




@Component
public class JWTManager
{

    private final JwtConstant jwtConstant;

    


    private JWTManager() {

        jwtConstant = new JwtConstant();
    }

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(jwtConstant.getSUBJECT())
                .withClaim(jwtConstant.getCLAIM(), username)
                .withIssuedAt(new Date())
                .withIssuer(jwtConstant.getORGANIZATION())
                .sign(Algorithm.HMAC256(jwtConstant.getSECRET()));
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtConstant.getSECRET()))
                .withSubject(jwtConstant.getSUBJECT())
                .withIssuer(jwtConstant.getORGANIZATION())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(jwtConstant.getCLAIM()).asString();
    }

}