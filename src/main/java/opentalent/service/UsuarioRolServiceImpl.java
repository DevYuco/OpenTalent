package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import opentalent.entidades.UsuarioRol;
import opentalent.entidades.UsuarioRolId;
import opentalent.repository.UsuarioRolRepository;

public class UsuarioRolServiceImpl implements UsuarioRolService  {
	
	@Autowired
	private UsuarioRolRepository usuarioRolRepository;

	@Override
	public List<UsuarioRol> buscarTodos() {
		
		return usuarioRolRepository.findAll() ;
	}

	@Override
	public UsuarioRol buscarUno(UsuarioRolId id) {
		
		return usuarioRolRepository.findById(id).orElse(null);
	}

	@Override
	public UsuarioRol insertUno(UsuarioRol ele) {
		try {
			if(!usuarioRolRepository.existsById(ele.getId())) {
				return usuarioRolRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(UsuarioRolId id) {
		try {
			if(usuarioRolRepository.existsById(id)) {
				usuarioRolRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public UsuarioRol modificarUno(UsuarioRol ele) {
		try {
			if(usuarioRolRepository.existsById(ele.getId())) {
				return usuarioRolRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
