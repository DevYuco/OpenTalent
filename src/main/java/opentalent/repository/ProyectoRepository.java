package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import opentalent.entidades.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer>{
	@Query("SELECT p FROM Proyecto p where activo = true")
	List<Proyecto> buscarTodosActivos(); 
	
	@Modifying
	@Transactional
	@Query("UPDATE Proyecto p SET p.activo = false WHERE p.idProyecto = ?1 ")
	Integer cancelarProyecto(int idProyecto);
}
