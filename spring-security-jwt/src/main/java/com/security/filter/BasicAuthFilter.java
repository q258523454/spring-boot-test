package com.security.filter;

import com.security.constant.CONSTANT;
import com.security.entity.Authority;
import com.security.exception.TokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 自定义过滤器
 */
public class BasicAuthFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthFilter.class);

    public BasicAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // 每次访问:从http头{MyAuthorization}项读取 {MyToken} 数据，然后用Jwts包提供的方法校验token的合法性
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("MyAuthorization");
        if (header == null || !header.startsWith("MyToken ")) {
            chain.doFilter(request, response);
            return;
        }
        // 校验{MyToken}
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("MyAuthorization");
        if (token == null || token.isEmpty()) {
            throw new TokenException("Token为空");
        }
        String user = null;
        try {
            long start = System.currentTimeMillis();
            user = Jwts.parser().setSigningKey(CONSTANT.SIGN_KEY)
                    .parseClaimsJws(token.replace("MyToken ", ""))
                    .getBody().getSubject();
            long end = System.currentTimeMillis();
            logger.info("解析token耗时:" + (end - start) + " 毫秒");
            if (user != null) {
                String[] split = user.split("-")[1].split(",");
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    authorities.add(new Authority(split[i]));
                }
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        } catch (ExpiredJwtException e) {
            logger.error("Token已过期: " + e);
            throw new TokenException("Token已过期");
        } catch (UnsupportedJwtException e) {
            logger.error("Token格式错误: " + e);
            throw new TokenException("Token格式错误");
        } catch (MalformedJwtException e) {
            logger.error("Token没有被正确构造: " + e);
            throw new TokenException("Token没有被正确构造");
        } catch (SignatureException e) {
            logger.error("签名失败: " + e);
            throw new TokenException("签名失败");
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常: " + e);
            throw new TokenException("非法参数异常");
        }
        return null;
    }

}
