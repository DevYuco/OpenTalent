package opentalent.repository;

import java.util.List;

public interface IGenericoCRUD<Entidad, Indice> {
	List<Entidad> buscarTodos(); 
	Entidad buscarUno(Indice id); 
	Entidad insertUno(Entidad ele); 
	int elimnarUno(Indice id); 
	Entidad modificarUno(Entidad ele);

}
