package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import opentalent.entidades.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, String>{
	@Query("select e from Empresa e where e.activo = true and destacado = true")
	List<Empresa> findByDestacadoYActivo(); 
	
	
}
