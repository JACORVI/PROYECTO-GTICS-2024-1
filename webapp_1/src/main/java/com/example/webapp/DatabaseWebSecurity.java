package com.example.webapp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select correo, contrasena, case when estado = 'Activo' then true else false end from usuario where correo = ?"
                )
                .authoritiesByUsernameQuery("select correo, rol from usuario where correo = ?")
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/registro/usuario" , "/assets/**").permitAll() 
                .antMatchers("/farmacista/**").hasAuthority("Farmacista")
                .antMatchers("/paciente/**").hasAuthority("Paciente")
                .antMatchers("/superadmin/**").hasAuthority("Superadmin")
                .antMatchers("/admin/**").hasAuthority("Administrador")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/acceso").permitAll()
                .failureUrl("/?error=true").and()
                .logout().permitAll().logoutSuccessUrl("/?logout")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // INDICAR QUE NO ESTÁ ENCRIPTADA - ver caso de enviar contraseña
    }
}