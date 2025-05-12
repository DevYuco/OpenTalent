package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Oferta;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioOfertaId;
import opentalent.repository.UsuarioOfertaRepository;
@Service
public class UsuarioOfertaServiceImpl implements UsuarioOfertaService {
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private UsuarioOfertaRepository usuarioOfertaRepository;
	
	@Autowired 
	private OfertaService ofertaService; 
	
	@Override
	public List<UsuarioOferta> buscarTodos() {
		
		return usuarioOfertaRepository.findAll();
	}

	@Override
	public UsuarioOferta buscarUno(UsuarioOfertaId id) {
		
		return usuarioOfertaRepository.findById(id).orElse(null);
	}

	@Override
	public UsuarioOferta insertUno(UsuarioOferta ele) {
		try {
			if(!usuarioOfertaRepository.existsById(ele.getId())) {
				return usuarioOfertaRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(UsuarioOfertaId id) {
		try {
			if(usuarioOfertaRepository.existsById(id)) {
				usuarioOfertaRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}
	@Override
	public UsuarioOferta modificarUno(UsuarioOferta ele) {
		try {
			if(usuarioOfertaRepository.existsById(ele.getId())) {
				return usuarioOfertaRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

	@Override
	public int añadirOfertaFavoritos(String username, int idOferta) {
		
		return usuarioOfertaRepository.añadirOfertaFavoritos(username, idOferta);
	}

	@Override
	public int eliminarOfertaFavoritos(String username, int idOferta) {
		
		return usuarioOfertaRepository.quitarOfertaFavoritos(username, idOferta);
	}

	@Override
	public Boolean comprobarFavorito(String username, int idOferta) {
		
		return usuarioOfertaRepository.comprobarFavorito(username, idOferta);
	}

	@Override
	public List<Oferta> buscarOfertasFavsActivasPorUsername(String username) {
		
		return usuarioOfertaRepository.buscarOfertasFavsActivasPorUsername(username);
	}

	@Override
	public int cambiarEstadoFavorito(boolean estado, String username, int idOferta) {
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);

	    if (usuario == null) {
	        return 0;
	    }

	    UsuarioOfertaId id = new UsuarioOfertaId(usuario.getIdUsuario(), idOferta);
	    UsuarioOferta usuarioOferta = usuarioOfertaRepository.findById(id).orElse(null);

	    if (usuarioOferta == null) {
	        // Si no existe, crear una relación SOLO como favorito
	        if (estado) {
	            UsuarioOferta nuevoFavorito = UsuarioOferta.builder()
	                .id(id)
	                .usuario(usuario)
	                .oferta(ofertaService.buscarUno(idOferta))
	                .estado(EstadoAplicacion.FAVORITO)
	                .propietario(false)
	                .favorito(true)
	                .build();
	            usuarioOfertaRepository.save(nuevoFavorito);
	            return 1;
	        } else {
	            // Si quiere desmarcar pero no existía, nada que hacer
	            return 0;
	        }
	    }

	    // Si ya existía inscripción o favorito
	    if (estado) {
	        usuarioOferta.setFavorito(true);
	        if (usuarioOferta.getEstado() == EstadoAplicacion.PENDIENTE || usuarioOferta.getEstado() == EstadoAplicacion.ACEPTADO || usuarioOferta.getEstado() == EstadoAplicacion.RECHAZADO) {
	            // Si ya está en proceso, solo setea favorito
	        } else {
	            usuarioOferta.setEstado(EstadoAplicacion.FAVORITO);
	        }
	    } else {
	        usuarioOferta.setFavorito(false);
	        if (usuarioOferta.getEstado() == EstadoAplicacion.FAVORITO) {
	            // Si estaba SOLO como favorito, eliminar la inscripción
	            usuarioOfertaRepository.delete(usuarioOferta);
	            return 1;
	        }
	    }

	    usuarioOfertaRepository.save(usuarioOferta);
	    return 1;
	}

	@Override
	public UsuarioOferta findByUsernameAndIdOferta(String username, int idOferta) {
		
		return usuarioOfertaRepository.findByUsernameAndIdOferta(username, idOferta);
	}

	@Override
	public int contarAceptadosPorOferta(int idOferta) {
		
		return usuarioOfertaRepository.contarAceptadosPorOferta(idOferta);
	}

	@Override
	public int contarInscritosPorEmpresa(String cif) {
		
		return usuarioOfertaRepository.contarInscritosPorEmpresa(cif);
	}

	@Override
	public boolean esFavorita(String username, int idOferta) {
		
		return usuarioOfertaRepository.esFavorita(username, idOferta);
	}

	@Override
	public boolean existeInscripcion(UsuarioOfertaId id) {
		
		return usuarioOfertaRepository.existsById(id);
	}

	@Override
	public List<Usuario> postulantesPendientes(int idOferta) {
		
		return usuarioOfertaRepository.postulantesPendientes(idOferta);
	}

	@Override
	public int cerrarPostulacionesPorOferta(int idOferta) {
		
		return usuarioOfertaRepository.cerrarPostulacionesPorOferta(idOferta);
	}

	@Override
	public int aceptarSolicitud(int idOferta, int idUsuario) {
		
		return usuarioOfertaRepository.aceptarSolicitud(idOferta, idUsuario);
	}

	@Override
	public int rechazarSolicitud(int idOferta, int idUsuario) {
		
		return usuarioOfertaRepository.rechazarSolicitud(idOferta, idUsuario);
	}
}
