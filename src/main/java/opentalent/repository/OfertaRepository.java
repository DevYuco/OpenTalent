package opentalent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import opentalent.entidades.Oferta;

public interface OfertaRepository extends JpaRepository<Oferta, Integer>{
	@Query("SELECT o FROM Oferta o WHERE o.estado = 'ACTIVA'")
	List<Oferta> buscarOfertasActivas();

}
