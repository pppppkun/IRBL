package pgd.irbl.business.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-30 15:33
 */
@Slf4j
public class JwtUtil {
    public static final String TOKEN_NAME = "irbl-token";
    // 秘钥
    static final String SECRET = "PGD";
    // 签名是有谁生成
    static final String ISSUSER = "IRBL";
    // 签名的主题
    static final String SUBJECT = "this is irbl token";
    // 签名的观众
    static final String AUDIENCE = "WEB";

    public static String createToken(Long userId,Integer expireMin) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> map = new HashMap<>();
            Date nowDate = new Date();
            // 过期时间 30min
            Date expireDate = getAfterDate(nowDate, 0, 0, expireMin, 0, 0, 0);
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            return JWT.create()
                    // 设置头部信息 Header
                    .withHeader(map)
                    // 设置 载荷 Payload
                    .withClaim("userId", userId)
                    .withIssuer(ISSUSER)
                    .withSubject(SUBJECT)
                    .withAudience(AUDIENCE)
                    // 生成签名的时间
                    .withIssuedAt(nowDate)
                    // 签名过期的时间
                    .withExpiresAt(expireDate)
                    // 签名 Signature
                    .sign(algorithm);
        } catch (
                JWTCreationException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    public static Long verifyTokenAndGetUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUSER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("userId");
            return claim.asLong();
        } catch (JWTVerificationException exception){
            log.error(exception.getMessage());
        }

        return null;
    }

    private static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }
}