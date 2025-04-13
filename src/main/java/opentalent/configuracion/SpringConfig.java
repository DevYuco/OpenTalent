package opentalent.configuracion;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SpringConfig {

	@Bean
	ModelMapper modelMapper() {
	    ModelMapper mapper = new ModelMapper();
	    
	    return mapper;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    //return new BCryptPasswordEncoder(); //Descomentar linea para contraseñas encriptadas
		 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
