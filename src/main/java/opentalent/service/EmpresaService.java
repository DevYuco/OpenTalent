package opentalent.service;

import java.util.List;

import opentalent.entidades.Empresa;
import opentalent.repository.IGenericoCRUD;

public interface EmpresaService extends IGenericoCRUD<Empresa, String>{
	List<Empresa> findByDestacadoYActivo(); 
}
