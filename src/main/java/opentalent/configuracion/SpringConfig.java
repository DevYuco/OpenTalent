package opentalent.configuracion;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import opentalent.dto.EmpresaDto;
import opentalent.dto.SectorNombreDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.Sector;

@Configuration
public class SpringConfig {

	@Bean
	ModelMapper modelMapper() {
	    ModelMapper mapper = new ModelMapper();

	    mapper.typeMap(Empresa.class, EmpresaDto.class).addMappings(m -> {
	        m.<List<SectorNombreDto>>map(
	            src -> {
	                List<Sector> sectores = src.getSectores();
	                return sectores == null
	                    ? List.of()
	                    : sectores.stream()
	                              .map(s -> new SectorNombreDto(s.getNombre()))
	                              .toList();
	            },
	            (dest, v) -> dest.setSectores(v)
	        );
	    });

	    return mapper;
	}
	@Bean
	PasswordEncoder passwordEncoder() {
	    //return new BCryptPasswordEncoder(); //Descomentar linea para contraseñas encriptadas
		 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
