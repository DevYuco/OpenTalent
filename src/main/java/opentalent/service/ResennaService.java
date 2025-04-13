package opentalent.service;

import java.util.List;

import opentalent.entidades.Resenna;
import opentalent.repository.IGenericoCRUD;

public interface ResennaService extends IGenericoCRUD<Resenna, Integer>{
	List<Resenna> findByEmpresaCif(String cif);
}
