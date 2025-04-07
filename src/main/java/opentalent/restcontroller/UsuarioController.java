package opentalent.restcontroller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import opentalent.dto.EmpresaDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.Oferta;
import opentalent.entidades.Proyecto;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.ProyectoService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
	
	@Autowired
	private EmpresaService empresaService; 
	
	@Autowired
	private OfertaService ofertaService; 
	
	@Autowired
	private ProyectoService proyectoService; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/home")
	public ResponseEntity<List<Empresa>> home() {
	    return ResponseEntity.ok(empresaService.findByDestacadoYActivo());
	}
	
	@GetMapping("/detallesempresa/{cif}")
	public ResponseEntity<EmpresaDto> detallesEmpresa(@PathVariable String cif) {
	    Empresa empresa = empresaService.buscarUno(cif);

	    if (empresa == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Mapear Empresa a EmpresaDto con sectores reducidos
	    EmpresaDto empresaDto = modelMapper.map(empresa, EmpresaDto.class);

	    return ResponseEntity.ok(empresaDto);
	}
	
	
	@GetMapping("/detallesoferta/{id}")
	public ResponseEntity<Oferta> detallesOferta(@PathVariable Integer id) {
	    Oferta oferta = ofertaService.buscarUno(id);

	    if (oferta == null) {
	        return ResponseEntity.notFound().build(); // 404 si no existe
	    }

	    return ResponseEntity.ok(oferta); // 200 OK con la oferta
	}
	
	@GetMapping("/ofertas")
	public ResponseEntity<List<Oferta>> todasOfertas(){
		return ResponseEntity.ok(ofertaService.buscarOfertasActivas());
	}
	
	@GetMapping("/proyectos")
	public ResponseEntity<List<Proyecto>> todosProyectos(){
		return ResponseEntity.ok(proyectoService.buscarTodosActivos()); 
	}
}
