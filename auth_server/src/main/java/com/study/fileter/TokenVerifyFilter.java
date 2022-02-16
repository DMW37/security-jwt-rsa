package com.study.fileter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.config.RsaKeyPropertiesConfig;
import com.study.domain.Payload;
import com.study.domain.SysUser;
import com.study.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 35612
 */
public class TokenVerifyFilter extends BasicAuthenticationFilter {
    private RsaKeyPropertiesConfig rsaKeyPropertiesConfig;

    public TokenVerifyFilter(AuthenticationManager authenticationManager, RsaKeyPropertiesConfig rsaKeyPropertiesConfig) {
        super(authenticationManager);
        this.rsaKeyPropertiesConfig = rsaKeyPropertiesConfig;
    }

    /**
     * 过滤请求，验证
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final boolean debug = this.logger.isDebugEnabled();

        String token = request.getHeader("Authorization");

        /**
         * 如果token异常
         */
        if (token == null || !token.toLowerCase().startsWith("Bearer ")) {
            chain.doFilter(request, response);
            PrintWriter writer = null;
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writer = response.getWriter();
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("code", HttpServletResponse.SC_FORBIDDEN);
                responseMap.put("msg", "请登入");
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
            return;
        }
        //token基本验证正常
        //通过token解析出载荷信息
        Payload<SysUser> userPayload = JwtUtils.getInfoFromToken(token.replaceFirst("Bearer ", ""), rsaKeyPropertiesConfig.getPublicKey(), SysUser.class);
        SysUser userInfo = userPayload.getUserInfo();
        //userInfo不为null 则返回
        if (userInfo != null) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userInfo.getUsername(), userInfo.getPassword(), userInfo.getRoleList());
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request, response);
    }
}
