package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Resenna;
import opentalent.repository.ResennaRepository;
@Service
public class ResennaServiceImpl implements ResennaService {
	
	@Autowired
	private ResennaRepository resennaRepository;

	@Override
	public List<Resenna> buscarTodos() {
		
		return resennaRepository.findAll();
	}

	@Override
	public Resenna buscarUno(Integer id) {
		
		return resennaRepository.findById(id).orElse(null);
	}

	@Override
	public Resenna insertUno(Resenna ele) {
		try {
			if(!resennaRepository.existsById(ele.getIdResenna())) {
				return resennaRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(resennaRepository.existsById(id)) {
				resennaRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}
	@Override
	public Resenna modificarUno(Resenna ele) {
		try {
			if(resennaRepository.existsById(ele.getIdResenna())) {
				return resennaRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
