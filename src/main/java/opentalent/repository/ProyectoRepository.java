package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import opentalent.entidades.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer>{
	@Query("SELECT p FROM Proyecto p where activo = true")
	List<Proyecto> buscarTodosActivos(); 
}
