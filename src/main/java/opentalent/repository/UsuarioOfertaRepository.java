package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import opentalent.entidades.Oferta;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioOfertaId;

public interface UsuarioOfertaRepository extends JpaRepository<UsuarioOferta, UsuarioOfertaId>{
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta u SET u.favorito = true WHERE u.usuario.username = ?1 and u.oferta.idOferta = ?2 ")
	int añadirOfertaFavoritos(String username, int idOferta); 
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta u SET u.favorito = false WHERE u.usuario.username = ?1 and u.oferta.idOferta = ?2 ")
	int quitarOfertaFavoritos(String username, int idOferta); 
	
	@Query("SELECT u.favorito FROM UsuarioOferta u WHERE u.usuario.username = ?1 AND u.oferta.idOferta = ?2")
	Boolean comprobarFavorito(String username, int idOferta); 
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta u SET u.favorito = ?1 WHERE u.usuario.username = ?2 AND u.oferta.idOferta = ?3 ")
	int cambiarEstadoFavorito(boolean estado, String username, int idOferta); 
	
	@Query("SELECT uo.oferta FROM UsuarioOferta uo WHERE (uo.oferta.estado = 'ACTIVA' OR uo.oferta.estado = 'PENDIENTE') AND uo.favorito = true AND uo.usuario.username = ?1")
	List<Oferta> buscarOfertasFavsActivasPorUsername(String username);
	
	@Query("SELECT uo FROM UsuarioOferta uo WHERE uo.usuario.username = ?1  AND uo.oferta.idOferta = ?2")
	UsuarioOferta findByUsernameAndIdOferta(String username, int idOferta);
	
	@Query("SELECT COUNT(uo) FROM UsuarioOferta uo WHERE uo.oferta.idOferta = ?1 and uo.estado = 'ACEPTADO'")
	int contarAceptadosPorOferta(int idOferta);
	
	@Query("SELECT COUNT(uo) FROM UsuarioOferta uo WHERE uo.estado = 'ACEPTADO' AND uo.oferta.empresa.cif = ?1") 
	int contarInscritosPorEmpresa(String cif);
	
	@Query("SELECT COUNT(uo) > 0 FROM UsuarioOferta uo WHERE uo.usuario.username = ?1 AND uo.oferta.idOferta = ?2  AND uo.favorito = true")
	boolean esFavorita(String username, int idOferta);

	@Query("SELECT uo.usuario FROM UsuarioOferta uo WHERE uo.estado = 'PENDIENTE' AND uo.oferta.idOferta = ?1 ")
	List<Usuario> postulantesPendientes(int idOferta); 
	
	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta uo SET uo.estado = 'RECHAZADO' WHERE uo.oferta.idOferta = ?1 ")
	int cerrarPostulacionesPorOferta(int idOferta);
	

	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta uo SET uo.estado = 'ACEPTADO' WHERE uo.oferta.idOferta = ?1 AND uo.usuario.idUsuario = ?2")
	int aceptarSolicitud(int idOferta, int idUsuario);

	@Modifying
	@Transactional
	@Query("UPDATE UsuarioOferta uo SET uo.estado = 'RECHAZADO' WHERE uo.oferta.idOferta = ?1 AND uo.usuario.idUsuario = ?2")
	int rechazarSolicitud(int idOferta, int idUsuario);
}
