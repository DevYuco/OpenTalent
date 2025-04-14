package opentalent.service;

import java.util.List;

import opentalent.dto.UsuarioVistaDetalleProyectoDto;
import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.UsuarioProyectoId;
import opentalent.repository.IGenericoCRUD;

public interface UsuarioProyectoService extends IGenericoCRUD<UsuarioProyecto, UsuarioProyectoId>{
	
	int añadirProyectoFavoritos(String username, int idProyecto); 
	int quitarProyectoFavoritos(String username, int idProyecto); 
	Boolean comprobarFavorito(String username, int idProyecto); 
	int cambiarEstadoFavorito(boolean estado, String username, int idProyecto); 
	List<Proyecto> buscarProyectosFavsActivosPorUsername(String username);
	List<Proyecto> buscarProyectosPropietarioYActivo(String username);
	List<Usuario> postulantesPendientes(int idProyecto); 
	int modificarEstadoProyecto(EstadoAplicacion estado, int idUsuario, int idProyecto);
	boolean esPropietarioDelProyecto(String username, int idProyecto);
	int rechazarSolicitudesPendientesPorProyecto(int idProyecto);
	boolean esFavorita(String username, int idProyecto);
	int contarInscritosPorProyecto(int idProyecto);
	UsuarioProyecto findByUsernameAndIdProyecto(String username, int idProyecto);
	List<UsuarioVistaDetalleProyectoDto> findUsuariosAceptadosByProyecto(int idProyecto);
}
