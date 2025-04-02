package opentalent.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import opentalent.dto.UsuarioDto;
import opentalent.entidades.Usuario;
import opentalent.repository.UsuarioRepository;
@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
    @Autowired
    private ModelMapper modelMapper;
    
	@Override
	public List<Usuario> buscarTodos() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario buscarUno(Integer id) {
		
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Usuario insertUno(Usuario ele) {
		try {
			if(!usuarioRepository.existsById(ele.getIdUsuario())) {
				return usuarioRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(usuarioRepository.existsById(id)) {
				usuarioRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Usuario modificarUno(Usuario ele) {
		try {
			if(usuarioRepository.existsById(ele.getIdUsuario())) {
				return usuarioRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

	@Override
	public UsuarioDto findByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
        
        if (usuario.getRol() != null) {
            dto.setRol(usuario.getRol().getNombre());
        }
        return dto;
	}

	@Override
	public boolean existsByUsername(String username) {
		
		return usuarioRepository.existsByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Usuario usuario = usuarioRepository.findByUsername(username);
	    if (usuario == null) {
	        throw new UsernameNotFoundException("Usuario no encontrado");
	    }
	    return usuario; // Funciona por que esta clase Ya implementa UserDetails
	}

	@Override
	public Usuario buscarPorUsernameEntidad(String username) {
		
		return usuarioRepository.findByUsername(username);
	}

	

}
