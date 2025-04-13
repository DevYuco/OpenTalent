package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Oferta;
import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioOfertaId;
import opentalent.repository.UsuarioOfertaRepository;
@Service
public class UsuarioOfertaServiceImpl implements UsuarioOfertaService {
	
	@Autowired
	private UsuarioOfertaRepository usuarioOfertaRepository;

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
		
		return usuarioOfertaRepository.cambiarEstadoFavorito(estado, username, idOferta);
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

}
