package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Rol;
import opentalent.repository.RolRepository;
@Service
public class RolServiceImpl implements RolService{
	
	@Autowired
	private RolRepository rolRepository;

	@Override
	public List<Rol> buscarTodos() {
		
		return rolRepository.findAll();
	}

	@Override
	public Rol buscarUno(Integer id) {
		
		return rolRepository.findById(id).orElse(null);
	}

	@Override
	public Rol insertUno(Rol ele) {
		try {
			if(!rolRepository.existsById(ele.getIdRol())) {
				return rolRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(rolRepository.existsById(id)) {
				rolRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Rol modificarUno(Rol ele) {
		try {
			if(rolRepository.existsById(ele.getIdRol())) {
				return rolRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
