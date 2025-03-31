package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.UsuarioProyectoId;
import opentalent.repository.UsuarioProyectoRepository;

public class UsuarioProyectoServiceImpl implements UsuarioProyectoService {
	
	@Autowired
	private UsuarioProyectoRepository usuarioProyectoRepository;

	@Override
	public List<UsuarioProyecto> buscarTodos() {
		
		return usuarioProyectoRepository.findAll();
	}

	@Override
	public UsuarioProyecto buscarUno(UsuarioProyectoId id) {
		
		return usuarioProyectoRepository.findById(id).orElse(null);
	}

	@Override
	public UsuarioProyecto insertUno(UsuarioProyecto ele) {
		try {
			if(!usuarioProyectoRepository.existsById(ele.getId())) {
				return usuarioProyectoRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(UsuarioProyectoId id) {
		try {
			if(usuarioProyectoRepository.existsById(id)) {
				usuarioProyectoRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}
	@Override
	public UsuarioProyecto modificarUno(UsuarioProyecto ele) {
		try {
			if(usuarioProyectoRepository.existsById(ele.getId())) {
				return usuarioProyectoRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
