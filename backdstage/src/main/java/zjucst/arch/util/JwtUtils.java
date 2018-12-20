package zjucst.arch.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    public static String generateToken(String username, Integer interval) {

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + interval * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, "ipcdn")
                .compact();
    }
}
