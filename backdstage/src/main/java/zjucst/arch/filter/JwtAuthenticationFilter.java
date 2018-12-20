package zjucst.arch.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zjucst.arch.Exception.MallException;
import zjucst.arch.auth.MallUserPrincipal;
import zjucst.arch.domain.VO.ResultVO;
import zjucst.arch.domain.entity.User;
import zjucst.arch.repository.UserRepository;
import zjucst.arch.util.JwtUtils;
import zjucst.arch.util.ResponseUtils;
import zjucst.arch.util.ResultVOUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (MallException e) {
            throw e;
        } catch (InternalAuthenticationServiceException e) {
            throw new MallException(10002, e.getMessage());
        } catch (Exception e) {
            throw new MallException(10001, "密码不正确");
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth) {
        String username = ((MallUserPrincipal) auth.getPrincipal()).getUsername();
        String token = JwtUtils.generateToken(username, 1);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        User user = userRepository.findByUsername(username);
        ResultVO resultVO = ResultVOUtils.success(user);
        String data = JSONObject.toJSON(resultVO).toString();
        ResponseUtils.responseWithStatus(response, data, -1);
    }
}
