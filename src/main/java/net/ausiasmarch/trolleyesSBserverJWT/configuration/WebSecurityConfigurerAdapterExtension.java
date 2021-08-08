/*
 * Copyright (c) 2021
 *
 * by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com) & DAW students
 *
 * TROLLEYES: Free Open Source Shopping Site
 *
 * Sources at:                https://github.com/rafaelaznar
 *
 * TROLLEYES is distributed under the MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.ausiasmarch.trolleyesSBserverJWT.configuration;

import net.ausiasmarch.trolleyesSBserverJWT.filter.CORSFilter;
import net.ausiasmarch.trolleyesSBserverJWT.filter.JwtOncePerRequestFilterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurerAdapterExtension extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPointImplementation oAuthenticationEntryPointImplementation;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtOncePerRequestFilterExtension jwtRequestFilter;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // configure AuthenticationManager so that it knows from where to load
//        // user for matching credentials
//        // Use BCryptPasswordEncoder
//        //auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//    //@SuppressWarnings("deprecation")
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//        //return NoOpPasswordEncoder.getInstance(); //<--deprecated,  declare PasswordEncoder as a bean
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CORSFilter corsFilter() {
        CORSFilter filter = new CORSFilter();
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
////        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
////        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.inMemoryAuthentication()
                .withUser("user").password((new BCryptPasswordEncoder().encode("123456"))).roles("USER").and()
                .withUser("manager").password((new BCryptPasswordEncoder().encode("123456"))).roles("MANAGER").and()
                .withUser("admin").password((new BCryptPasswordEncoder().encode("123456"))).roles("ADMIN", "MANAGER", "USER");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // if not using APIs annotations use this configuration
        // to asign centralized access permissions
//        httpSecurity
//                .csrf().disable()
//                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
//                // dont authenticate this particular requests
//                .authorizeRequests()
//                .antMatchers("/authenticate").permitAll()
//                .antMatchers(HttpMethod.GET, "/producto/**").permitAll()
//                // all other requests need to be authenticated
//                //.antMatchers("/usuario").hasAuthority("ADMIN") -- example with roles
//                .anyRequest().authenticated()
//                .and().exceptionHandling().authenticationEntryPoint(oAuthenticationEntryPointImplementation)
//                // use stateless session; session won't be used to store user's state.
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // to use de-centralized permissions use this configuration
        // with API annotations
        httpSecurity
                .csrf().disable()
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .authorizeRequests()
                .anyRequest().permitAll()
                .and().exceptionHandling().authenticationEntryPoint(oAuthenticationEntryPointImplementation)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add the filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
