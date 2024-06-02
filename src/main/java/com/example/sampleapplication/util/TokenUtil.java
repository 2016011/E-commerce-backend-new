package com.example.sampleapplication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenUtil {
    private static final long TOKEN_VALIDITY =  2 * 60L;
    private static final long REFRESH_TOKEN_VALIDITY = 24 * 60 * 60L;

    private static Algorithm algorithm = Algorithm.HMAC256("myString");

    private static Algorithm refreshTokenAlgorithm = Algorithm.HMAC256("refreshTokenString");

    public TokenUtil() {
    }

    public static String generateToken(Long userID) {
        return JWT.create().withIssuer("base_auth").withClaim("userId", userID).withIssuedAt(new Date(System.currentTimeMillis())).withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)).sign(algorithm);
    }

    public static String generateRefreshToken(Long userId){
        return JWT.create()
                .withIssuer("base_auth")
                .withClaim("userId", userId)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000))
                .sign(refreshTokenAlgorithm);
    }

    public static Long getUserIDFromToken(String token) {
        System.out.println("getUserIDFromToken start");
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        return decodedJWT.getClaim("userId").asLong();
    }

    public static Algorithm getSignAlgorithm() {
        return algorithm;
    }

    public static Long getUserIdFromHeader(String header) {
        String token = header.split(" ")[1];
        return getUserIDFromToken(token);
    }

    public static Long getUserIDFromRefreshToken(String refreshToken) {
        System.out.println("getUserIDFromRefreshToken start : " + refreshToken);
        DecodedJWT decodedJWT = JWT.require(refreshTokenAlgorithm).build().verify(refreshToken);
        Long userId =  decodedJWT.getClaim("userId").asLong();
        System.out.println("getUserIDFromRefreshToken userId : " + userId);
        return userId;
    }

    public static String refreshRefreshToken(String refreshToken) {
        System.out.println("refreshRefreshToken start : " + refreshToken);
        String token = JWT.create()
                .withIssuer("base_auth")
                .withClaim("userId", getUserIDFromRefreshToken(refreshToken))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000))
                .sign(refreshTokenAlgorithm);
        System.out.println("refreshRefreshToken new refreshToken : " + token);
        return  token;
    }

    public static String refreshToken(String token) {
        return JWT.create()
                .withIssuer("base_auth")
                .withClaim("userId", getUserIDFromToken(token))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .sign(algorithm);
    }
}