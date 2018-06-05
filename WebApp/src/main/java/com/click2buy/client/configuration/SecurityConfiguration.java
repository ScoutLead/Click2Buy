package com.click2buy.client.configuration;

import com.click2buy.client.security.JWTAuthenticationFilter;
import com.click2buy.client.security.JwtAuthenticationEntryPoint;
import com.click2buy.client.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserDetailsService userDetailsService;

  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.route.authentication.path}")
  private String authenticationPath;

  public SecurityConfiguration(
    JwtAuthenticationEntryPoint unauthorizedHandler,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    UserServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.unauthorizedHandler = unauthorizedHandler;
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      // we don't need CSRF because our token is invulnerable
      .csrf().disable()

      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

      // don't create session
      //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

      .authorizeRequests()

      // Un-secure H2 Database
      .antMatchers("/h2-console/**/**").permitAll()

      .antMatchers("/auth/**").permitAll()
      .anyRequest().authenticated();

    // Custom JWT based security filter
    http
      .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    // disable page caching
    http
      .headers()
      .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
      .cacheControl();
  }

  @Override
  public void configure(WebSecurity web) {
    // AuthenticationTokenFilter will ignore the below paths
    web
      .ignoring()
      .antMatchers(
        HttpMethod.POST,
        authenticationPath
      )

      // allow anonymous resource requests
      .and()
      .ignoring()
      .antMatchers(
        HttpMethod.GET,
        "/",
        "/*.html",
        "/bucket/**",
        "/image/**",
        "/goods/**",
        "/favicon.ico",
        "/**/*.html",
        "/**/*.css",
        "/**/*.png",
        "/**/*.js"
      )

      // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
      .and()
      .ignoring()
      .antMatchers("/h2-console/**/**");
  }

}
