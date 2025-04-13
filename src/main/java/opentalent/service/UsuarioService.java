package opentalent.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import opentalent.dto.UsuarioDto;
import opentalent.entidades.Usuario;
import opentalent.repository.IGenericoCRUD;

public interface UsuarioService extends IGenericoCRUD<Usuario, Integer>, UserDetailsService{
	UsuarioDto findByUsername(String username);
	boolean existsByUsername(String username);
	Usuario buscarPorUsernameEntidad(String username); 
	
}
