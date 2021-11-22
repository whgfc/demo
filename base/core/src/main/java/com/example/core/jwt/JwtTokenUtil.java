package com.example.core.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.context.constant.ConstantContextHolder;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * @author demo
 * @description
 */
public class JwtTokenUtil {

    /**
     * 生成token
     * @param jwtPayLoad
     * @return
     */
    public static String generateToken (JwtPayLoad jwtPayLoad) {
        DateTime expirationDate = DateUtil.offsetSecond(new Date(),
                Convert.toInt(ConstantContextHolder.getTokenExpireSec()));
        return Jwts.builder()
                .setClaims(BeanUtil.beanToMap(jwtPayLoad))
                .setSubject(jwtPayLoad.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 根据token获取Claims
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取JwtPayLoad
     * @param token
     * @return
     */
    public static JwtPayLoad getJwtPayLoad(String token) {
        Claims claims = getClaimsFromToken(token);
        return BeanUtil. toBeanIgnoreCase(claims, JwtPayLoad.class, false);
    }

    /**
     * 校验token是否正确
     */
    public static Boolean checkToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    /**
     * 校验token是否失效
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            final Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 获取解密Key
     * @return
     */
    public static Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(ConstantContextHolder.getJwtSecret()));
    }
}
