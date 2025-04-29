 package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.UsuarioProyectoId;

public interface UsuarioProyectoRepository extends JpaRepository<UsuarioProyecto, UsuarioProyectoId>{
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioProyecto u SET u.favorito = true WHERE u.usuario.username = ?1 and u.proyecto.idProyecto = ?2 ")
	int añadirProyectoFavoritos(String username, int idProyecto); 
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioProyecto u SET u.favorito = false WHERE u.usuario.username = ?1 and u.proyecto.idProyecto = ?2 ")
	int quitarProyectoFavoritos(String username, int idProyecto); 
	
	@Query("SELECT u.favorito FROM UsuarioProyecto u WHERE u.usuario.username = ?1 AND u.proyecto.idProyecto = ?2")
	Boolean comprobarFavorito(String username, int idProyecto); 
	
	@Query("SELECT up.proyecto FROM UsuarioProyecto up WHERE up.proyecto.activo = true AND up.favorito = true AND up.usuario.username = ?1 ")
	List<Proyecto> buscarProyectosFavsActivosPorUsername(String username); 
	
	@Query("SELECT up.proyecto FROM UsuarioProyecto up WHERE up.proyecto.activo = true AND up.propietario = true AND up.usuario.username = ?1 ")
	List<Proyecto> buscarProyectosPropietarioYActivo(String username); 
	
	@Query("SELECT up.usuario FROM UsuarioProyecto up WHERE up.estado = 'PENDIENTE' AND up.proyecto.idProyecto = ?1 ")
	List<Usuario> postulantesPendientes(int idProyecto); 
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioProyecto up SET up.estado = ?1 WHERE up.usuario.idUsuario = ?2 AND up.proyecto.idProyecto = ?3")
	int modificarEstadoProyecto(EstadoAplicacion estado, int idUsuario, int idProyecto); 
	
	@Query("SELECT COUNT(up) > 0 FROM UsuarioProyecto up WHERE up.usuario.username = ?1 AND up.proyecto.idProyecto = ?2 AND up.propietario = true")
	boolean esPropietarioDelProyecto(String username, int idProyecto);
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioProyecto up SET up.estado = 'RECHAZADO' WHERE up.proyecto.idProyecto = ?1 ")
	int rechazarSolicitudesPendientesPorProyecto(int idProyecto);
	
	@Query("SELECT COUNT(up) > 0 FROM UsuarioProyecto up WHERE up.usuario.username = ?1 AND up.proyecto.idProyecto = ?2 AND up.favorito = true")
	boolean esFavorita(String username, int idProyecto);
	
	@Query("SELECT COUNT(up) FROM UsuarioProyecto up WHERE up.estado = 'ACEPTADO' AND up.proyecto.idProyecto = ?1") 
	int contarInscritosPorProyecto(int idProyecto);
	
	@Query("SELECT up FROM UsuarioProyecto up WHERE up.usuario.username = ?1  AND up.proyecto.idProyecto = ?2")
	UsuarioProyecto findByUsernameAndIdProyecto(String username, int idProyecto);
	
	@Query("SELECT up.usuario FROM UsuarioProyecto up WHERE up.proyecto.idProyecto = ?1 AND up.estado = 'ACEPTADO'") 
	List<Usuario> findUsuariosAceptadosByProyecto(int idProyecto);
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioProyecto u SET u.favorito = ?1 WHERE u.usuario.username = ?2 AND u.proyecto.idProyecto = ?3 ")
	int cambiarEstadoFavorito(boolean estado, String username, int idProyecto);
	
	@Query("SELECT up.usuario FROM UsuarioProyecto up WHERE up.proyecto.id = ?1 AND up.propietario = true")
	Usuario findPropietarioByProyecto( int idProyecto);
}
