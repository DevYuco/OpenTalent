package opentalent.service;

import java.util.List;

import opentalent.entidades.Oferta;
import opentalent.repository.IGenericoCRUD;

public interface OfertaService extends IGenericoCRUD<Oferta, Integer>{
	List<Oferta> buscarOfertasActivas();
	List<Oferta> findActivasByEmpresaCif(String cif);
}
