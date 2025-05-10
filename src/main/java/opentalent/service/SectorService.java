package opentalent.service;

import opentalent.entidades.Sector;
import opentalent.repository.IGenericoCRUD;

public interface SectorService extends IGenericoCRUD<Sector, Integer>{
	Sector buscarPorNombre(String nombre);


}
