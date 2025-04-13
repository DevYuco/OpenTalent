package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import opentalent.entidades.Resenna;

public interface ResennaRepository extends JpaRepository<Resenna, Integer>{
	@Query("SELECT r FROM Resenna r WHERE r.empresa.cif = :cif")
	List<Resenna> findByEmpresaCif(String cif);
}
