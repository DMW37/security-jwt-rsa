package com.study.fileter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.config.RsaKeyPropertiesConfig;
import com.study.domain.SysRole;
import com.study.domain.SysUser;
import com.study.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 35612
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    private RsaKeyPropertiesConfig rsaKeyPropertiesConfig;

    public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyPropertiesConfig rsaKeyPropertiesConfig) {
        this.authenticationManager = authenticationManager;
        this.rsaKeyPropertiesConfig = rsaKeyPropertiesConfig;
    }

    /**
     * 接收并解析用户凭证，出現错误时，返回json数据前端
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            //认证成功
            SysUser sysUser = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    sysUser.getUsername(), sysUser.getPassword());


            return this.authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            PrintWriter writer = null;
            try {
                //认证失败
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer = response.getWriter();
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                responseMap.put("message", "账号或者密码有误");
                String responseResult = new ObjectMapper().writeValueAsString(responseMap);
                writer.write(responseResult);
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            throw new RuntimeException(e);
        }
    }


    /**
     * 用户登录成功后，生成token,并且返回json数据给前端
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
//得到当前认证的用户对象
        SysUser sysUser = new SysUser();
        sysUser.setUsername(authResult.getName());
        sysUser.setRoleList((List<SysRole>) authResult.getAuthorities());
//json web token构建
        String token = JwtUtils.generateTokenExpireInMinutes(sysUser, rsaKeyPropertiesConfig.getPrivateKey(), 60 * 24);
        response.addHeader("Authorization", "Bearer " + token);
        //登录成功時，返回json格式进行提示
        PrintWriter writer = null;
        try {
            //认证失败
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            writer = response.getWriter();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("code", HttpServletResponse.SC_OK);
            responseMap.put("message", "登入成功");
            String responseResult = new ObjectMapper().writeValueAsString(responseMap);
            writer.write(responseResult);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }


}
