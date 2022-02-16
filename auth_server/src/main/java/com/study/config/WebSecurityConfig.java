package com.study.config;

import com.study.fileter.JwtLoginFilter;
import com.study.fileter.JwtVerifyFilter;
import com.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RsaKeyProperties prop;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //指定认证对象的来源
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService).passwordEncoder(passwordEncoder());
    }
    //SpringSecurity配置信息
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //关闭夸张请求保护
                .cors().and().csrf().disable()
                //允许不登入授权访问
            .authorizeRequests()
            .antMatchers("/product").hasAnyRole("USER")
                //其他资源需要授权后访问
            .anyRequest().authenticated()
            .and()
                //添加过滤器
            .addFilter(new JwtLoginFilter(super.authenticationManager(), prop))
            .addFilter(new JwtVerifyFilter(super.authenticationManager(), prop))
                //前后端分离是无状态的，直接禁用session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
