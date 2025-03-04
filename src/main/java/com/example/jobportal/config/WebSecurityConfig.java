package com.example.jobportal.config;

import com.example.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // It is helping for the configuration of the application
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; //It is also helping for the configuration of the application and
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // filter chain that is responsible for the security of the application.


@Configuration
public class WebSecurityConfig {

    private  final CustomUserDetailsService customUserDetailsService;
    private  final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    private final String[] publicUrl = {"/", //
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"
    };

    @Bean //protected can change to public
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // It is used to create the security filter chain.

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth->
                {
                    auth.requestMatchers(publicUrl).permitAll(); // permitAll() method is used to allow all the requests. and requestMatchers() method is used to match the request. so here we are allowing all the requests.
                    auth.anyRequest().authenticated(); // anyRequest() method is used to match any request and authenticated() method is used to check the authentication of the request.
                }
        );

        http.formLogin(form -> form.loginPage("/login").permitAll().successHandler(customAuthenticationSuccessHandler))
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login");
                }).cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());//passing the login page and the success handler. loginPage() method is used to pass the login page. permitAll() method is used to allow all the requests. successHandler() method is used to pass the success handler.

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); //dao means data access object. DaoAuthenticationProvider() is used to provide the authentication of the user.
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // passwordEncoder() method is used to encode the password. encode means to convert the password into the hash code. hash code means the password is converted into the unreadable format. example unreadeable format is 1234 is converted into 1234@#$%.
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider; //I added additonally this line
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder() is used to encode the password. encode means to convert the password into the hash code. hash code means the password is converted into the unreadable format. example unreadeable format is 1234 is converted into 1234@#$%.
    } //the reason of the two encoder is that the password is encoded in the database and the password are encoded in the application. so that the password is not visible to the user and the hacker.
}
