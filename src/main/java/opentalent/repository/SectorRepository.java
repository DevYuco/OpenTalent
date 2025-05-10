package opentalent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import opentalent.entidades.Sector;

public interface SectorRepository extends JpaRepository<Sector, Integer>{
	@Query("SELECT s FROM Sector s WHERE s.nombre = ?1")
	Sector findByName(String nombre);





}
