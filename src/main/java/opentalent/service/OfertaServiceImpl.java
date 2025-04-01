package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import opentalent.entidades.Oferta;
import opentalent.repository.OfertaRepository;
@Service
public class OfertaServiceImpl implements OfertaService {
	
	@Autowired
	private OfertaRepository ofertaRepository;

	@Override
	public List<Oferta> buscarTodos() {
		
		return ofertaRepository.findAll();
	}

	@Override
	public Oferta buscarUno(Integer id) {
		
		return ofertaRepository.findById(id).orElse(null);
	}

	@Override
	public Oferta insertUno(Oferta ele) {
		try {
			if(!ofertaRepository.existsById(ele.getIdOferta())) {
				return ofertaRepository.save(ele);
			}
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(ofertaRepository.existsById(id)) {
				ofertaRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Oferta modificarUno(Oferta ele) {
		try {
			if(ofertaRepository.existsById(ele.getIdOferta())) {
				return ofertaRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
