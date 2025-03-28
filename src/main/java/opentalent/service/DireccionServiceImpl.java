package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Direccion;
import opentalent.repository.DireccionRepository;

@Service
public class DireccionServiceImpl implements DireccionService {
	@Autowired
	private DireccionRepository direccionRepository; 
	
	@Override
	public List<Direccion> buscarTodos() {
		
		return direccionRepository.findAll();
	}

	@Override
	public Direccion buscarUno(Integer id) {
		return direccionRepository.findById(id).orElse(null);
	}

	@Override
	public Direccion insertUno(Direccion ele) {
		try {
			if(!direccionRepository.existsById(ele.getIdDireccion())) {
				return direccionRepository.save(ele);
			}
			return null;
		} catch (Exception e) {
			return null; 
		}
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(direccionRepository.existsById(id)) {
				direccionRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Direccion modificarUno(Direccion ele) {
		try {
			if(direccionRepository.existsById(ele.getIdDireccion())) {
				return direccionRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
