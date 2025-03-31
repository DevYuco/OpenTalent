package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import opentalent.entidades.Usuario;
import opentalent.repository.UsuarioRepository;

public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

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

}
