package zjucst.arch.filter;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import zjucst.arch.Exception.MallException;
import zjucst.arch.auth.MallUserPrincipal;
import zjucst.arch.domain.entity.User;
import zjucst.arch.repository.UserRepository;
import zjucst.arch.util.ResponseUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        // parse the token.
        try {
            String user = Jwts.parser()
                    .setSigningKey("ipcdn")
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();
            User u = userRepository.findByUsername(user);
            if (u == null) {
                logger.error(user + " is not exist!");
                ResponseUtils.responseWithStatus(response, user + " is not exist!", 403);
                throw new MallException(403, user + " is not exist");
            }
            if (user != null) {
                UsernamePasswordAuthenticationToken upat =
                        new UsernamePasswordAuthenticationToken(
                                new MallUserPrincipal(u),
                                null,
                                Collections.emptyList());
                upat.setDetails(u);
                return upat;
            }
        } catch (ExpiredJwtException e) {
            logger.error("Token已过期: {} " + e);
            throw new MallException(20001, "Token已过期");
        } catch (UnsupportedJwtException e) {
            logger.error("Token格式错误: {} " + e);
            throw new MallException(20002, "Token格式错误");
        } catch (MalformedJwtException e) {
            logger.error("Token没有被正确构造: {} " + e);
            throw new MallException(20003, "Token没有被正确构造");
        } catch (SignatureException e) {
            logger.error("签名失败: {} " + e);
            throw new MallException(20004, "签名失败");
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常: {} " + e);
            throw new MallException(20005, "非法参数异常");
        }

        return null;
    }
}
