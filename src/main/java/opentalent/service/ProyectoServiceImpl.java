package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Proyecto;
import opentalent.repository.ProyectoRepository;
@Service
public class ProyectoServiceImpl implements ProyectoService{
	
	@Autowired
	private ProyectoRepository proyectoRepository;

	@Override
	public List<Proyecto> buscarTodos() {
		
		return proyectoRepository.findAll();
	}

	@Override
	public Proyecto buscarUno(Integer id) {
		
		return proyectoRepository.findById(id).orElse(null);
	}

	@Override
	public Proyecto insertUno(Proyecto ele) {
		try {
			if(!proyectoRepository.existsById(ele.getIdProyecto())) {
				return proyectoRepository.save(ele);
			}
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(proyectoRepository.existsById(id)) {
				proyectoRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Proyecto modificarUno(Proyecto ele) {
		try {
			if(proyectoRepository.existsById(ele.getIdProyecto())) {
				return proyectoRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}


}
