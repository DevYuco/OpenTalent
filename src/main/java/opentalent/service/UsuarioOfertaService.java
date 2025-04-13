package opentalent.service;

import java.util.List;

import opentalent.entidades.Oferta;
import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioOfertaId;
import opentalent.repository.IGenericoCRUD;

public interface UsuarioOfertaService extends IGenericoCRUD<UsuarioOferta, UsuarioOfertaId>{
	int añadirOfertaFavoritos(String username, int idOferta); 
	int eliminarOfertaFavoritos(String username, int idOferta); 
	Boolean comprobarFavorito(String username, int idOferta);
	int cambiarEstadoFavorito(boolean estado, String username, int idOferta); 
	List<Oferta> buscarOfertasFavsActivasPorUsername(String username); 
	UsuarioOferta findByUsernameAndIdOferta(String username, int idOferta);
	int contarAceptadosPorOferta(int idOferta);
	int contarInscritosPorEmpresa(String cif);
	boolean esFavorita(String username, int idOferta);
}
