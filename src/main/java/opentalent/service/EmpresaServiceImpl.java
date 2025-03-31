package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import opentalent.entidades.Empresa;
import opentalent.repository.EmpresaRepository;

public class EmpresaServiceImpl implements EmpresaService {
	
	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public List<Empresa> buscarTodos() {
		
		return empresaRepository.findAll() ;
	}

	@Override
	public Empresa buscarUno(String id) {
		
		return empresaRepository.findById(id).orElse(null);
	}

	@Override
	public Empresa insertUno(Empresa ele) {
		try {
			if(!empresaRepository.existsById(ele.getCif())) {
				return empresaRepository.save(ele);
			}
			return null;
		} catch (Exception e) {
			return null; 
		}
	}

	@Override
	public int elimnarUno(String id) {
		try {
			if(empresaRepository.existsById(id)) {
				empresaRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Empresa modificarUno(Empresa ele) {
		try {
			if(empresaRepository.existsById(ele.getCif())) {
				return empresaRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
