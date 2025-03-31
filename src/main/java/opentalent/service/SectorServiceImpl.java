package opentalent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import opentalent.entidades.Sector;
import opentalent.repository.SectorRepository;

public class SectorServiceImpl implements SectorService {
	
	@Autowired
	private SectorRepository sectorRepository;

	@Override
	public List<Sector> buscarTodos() {
		
		return sectorRepository.findAll();
	}

	@Override
	public Sector buscarUno(Integer id) {
		
		return sectorRepository.findById(id).orElse(null);
	}

	@Override
	public Sector insertUno(Sector ele) {
		try {
			if(!sectorRepository.existsById(ele.getIdSector())) {
				return sectorRepository.save(ele);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return null;
	}

	@Override
	public int elimnarUno(Integer id) {
		try {
			if(sectorRepository.existsById(id)) {
				sectorRepository.deleteById(id);
				return 1; 
			}
			return 0; 
		} catch (Exception e) {
			return -1;
		}	
	}

	@Override
	public Sector modificarUno(Sector ele) {
		try {
			if(sectorRepository.existsById(ele.getIdSector())) {
				return sectorRepository.save(ele); 
			}
			return null; 
		} catch (Exception e) {
			return null; 
		}
	}

}
