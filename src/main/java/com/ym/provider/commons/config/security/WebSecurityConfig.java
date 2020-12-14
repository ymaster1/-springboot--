package com.ym.provider.commons.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.PrintWriter;

/**
 * @author ymaster1
 * @date 2020/11/19 15:26
 * @description security配置类
 * //    用户信息
 * //    http拦截
 * //    密码编码器
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 必须指向自己的，不然就是默认的，白搞
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        /**
         * 如有需要，可以将resource下的指定静态资源忽略，全部放行
         */
        web.ignoring()
                .antMatchers("/js/**", "/css/**", "images/**");
    }

    /**
     * 安全拦截，拦截指定路径，链式编程
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//       认证请求 匹配顺序从上到下，别写错
        http.authorizeRequests()
//                                所有人都可以访问的路径
                .antMatchers("/", "/test/**").permitAll()
//                /table/task/**下需要vip角色和admin权限才可以访问
//                .antMatchers("/abc/**").hasRole("vip");
//                多个角色之一
                .antMatchers("/table/task/**").hasAnyRole("ROLE_vip")
//                多个权限之一
                .antMatchers("/table/task/**").hasAnyAuthority("admin")
//                其他所有请求都需要认证,只要登陆成功就能访问，不需要角色和权限，只能在最后
                .anyRequest().authenticated();
        /**
         * 登录表单
         */
        http.formLogin()
//                登陆页面发送action的地址,
                .loginProcessingUrl("/doLogin")
//                指定自己的登陆页面，不用默认的，前后端分离后不需要了
//                .loginPage("login.html")
//                登陆表单的参数，可以自定义
                .usernameParameter("name")
                .passwordParameter("password")
//                登陆成功之后服务端跳转到指定路径，地址栏不变还是doLogin
//                .successForwardUrl("/getConfig")
//                登陆成功之后跳转到指定路径，重定向恢复到登陆之后应该去的路径，前后端不分离只需要配置其中一个
//                .defaultSuccessUrl("/getConfig")
//                前后端分离，成功返回json
                .successHandler((req, resp, auth) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = resp.getWriter();
//                    成功返回个人信息，可以自己定义
                    writer.write(new ObjectMapper().writeValueAsString(auth.getPrincipal()));
                    writer.flush();
                    writer.close();
                })
//                前后端分离，失败返回json
                .failureHandler((req, resp, exception) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = resp.getWriter();
//                    成功返回个人信息，可以自己定义,可以根据异常类型自定义返回值，替换掉这个message就行
                    writer.write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                    writer.flush();
                    writer.close();
                })
//                登录请求都允许通过
                .permitAll();
/**
 * 登出
 */
        http.logout()
//                默认就是/logout,get请求，可以自定义
                .logoutUrl("/logout")
//                第二个参数就请求方式
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","POST"))
                .logoutSuccessHandler((req, resp, exception) -> {
                    resp.setContentType("application/json;charset=utf-8");
//            可以设置状态码
//            resp.setStatus(401);
                    PrintWriter writer = resp.getWriter();
//                    成功返回个人信息，可以自己定义,可以根据异常类型自定义返回值，替换掉这个message就行
                    writer.write(new ObjectMapper().writeValueAsString("注销成功"));
                    writer.flush();
                    writer.close();
                })
                .permitAll();

/**
 * 未授权访问
 */
        http.csrf().disable()
//                设置未授权访问，提示未登录
                .exceptionHandling().authenticationEntryPoint((req, resp, exception) -> {
            resp.setContentType("application/json;charset=utf-8");
//            可以设置状态码
//            resp.setStatus(401);
            PrintWriter writer = resp.getWriter();
//                    成功返回个人信息，可以自己定义,可以根据异常类型自定义返回值，替换掉这个message就行
            writer.write(new ObjectMapper().writeValueAsString("请先登录"));
            writer.flush();
            writer.close();
        });

    }

    /**
     * 用户信息
     * 新版需要加入密码编码，不允许使用明文
     *
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        使用内存存储用户，不过一般都是使用数据库,通过and可以无限添加角色
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("ymaster1").password(new BCryptPasswordEncoder().encode("123456")).roles("vip");
////                .and()
////                .withUser()
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        注入自己的service
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    /**
     * security5之后强制要求密码需要加密，可以自定义加密规则，实现该接口就行
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
//        角色继承，admin自动继承user角色的所有权限，然后可以再做扩展
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return roleHierarchy;
    }
}