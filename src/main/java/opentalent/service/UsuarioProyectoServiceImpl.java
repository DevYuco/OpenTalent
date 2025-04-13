package opentalent.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.dto.UsuarioVistaDetalleProyectoDto;
import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.UsuarioProyectoId;
import opentalent.repository.UsuarioProyectoRepository;
@Service
public class UsuarioProyectoServiceImpl implements UsuarioProyectoService {
    @Autowired
    private ModelMapper modelMapper;
    
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

	@Override
	public int añadirProyectoFavoritos(String username, int idProyecto) {
		
		return usuarioProyectoRepository.añadirProyectoFavoritos(username, idProyecto);
	}

	@Override
	public int quitarProyectoFavoritos(String username, int idProyecto) {
		
		return usuarioProyectoRepository.quitarProyectoFavoritos(username, idProyecto);
	}

	@Override
	public Boolean comprobarFavorito(String username, int idProyecto) {
		
		return usuarioProyectoRepository.comprobarFavorito(username, idProyecto);
	}

	@Override
	public List<Proyecto> buscarProyectosFavsActivosPorUsername(String username) {
		
		return usuarioProyectoRepository.buscarProyectosFavsActivosPorUsername(username);
	}

	@Override
	public List<Proyecto> buscarProyectosPropietarioYActivo(String username) {
		
		return usuarioProyectoRepository.buscarProyectosPropietarioYActivo(username);
	}

	@Override
	public List<Usuario> postulantesPendientes(int idProyecto) {
		
		return usuarioProyectoRepository.postulantesPendientes(idProyecto);
	}

	@Override
	public int modificarEstadoProyecto(EstadoAplicacion estado, int idUsuario, int idProyecto) {
		
		return usuarioProyectoRepository.modificarEstadoProyecto(estado, idUsuario, idProyecto);
	}

	@Override
	public boolean esPropietarioDelProyecto(String username, int idProyecto) {
		
		return usuarioProyectoRepository.esPropietarioDelProyecto(username, idProyecto);
	}

	@Override
	public int rechazarSolicitudesPendientesPorProyecto(int idProyecto) {
		
		return usuarioProyectoRepository.rechazarSolicitudesPendientesPorProyecto(idProyecto);
	}

	@Override
	public boolean esFavorita(String username, int idProyecto) {
		
		return usuarioProyectoRepository.esFavorita(username, idProyecto);
	}

	@Override
	public int contarInscritosPorProyecto(int idProyecto) {
		
		return usuarioProyectoRepository.contarInscritosPorProyecto(idProyecto);
	}

	@Override
	public UsuarioProyecto findByUsernameAndIdProyecto(String username, int idProyecto) {
		
		return usuarioProyectoRepository.findByUsernameAndIdProyecto(username, idProyecto);
	}

	@Override
	public List<UsuarioVistaDetalleProyectoDto> findUsuariosAceptadosByProyecto(int idProyecto) {
		List<Usuario> usuarios = usuarioProyectoRepository.findUsuariosAceptadosByProyecto(idProyecto);
		
		List<UsuarioVistaDetalleProyectoDto> usuariosDto = usuarios.stream()
				.map(u -> modelMapper.map(u,UsuarioVistaDetalleProyectoDto.class))
				.toList(); 
		
		return usuariosDto;
	}

}
