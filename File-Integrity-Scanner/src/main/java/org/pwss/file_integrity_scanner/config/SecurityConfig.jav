import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/file-integrity/**").authenticated()  // All endpoints under /api/file-integrity
require authentication
            .and()
            .httpBasic();  // Use basic HTTP authentication for simplicity

        // TODO (in future) : use OAuth2,  JWT tokens or something similar
    }
}