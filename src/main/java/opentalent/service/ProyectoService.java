package opentalent.service;

import java.util.List;

import opentalent.entidades.Proyecto;
import opentalent.repository.IGenericoCRUD;

public interface ProyectoService extends IGenericoCRUD<Proyecto, Integer>{
	List<Proyecto> buscarTodosActivos(); 
	Integer cancelarProyecto(int idProyecto);
}
