package opentalent.restcontroller.usuario;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.EmpresaHomeUsuarioDto;
import opentalent.entidades.Empresa;
import opentalent.service.EmpresaService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
@Tag(name = "10 - Usuario - Home", description = "Endpoints para visualizar home desde el perfil usuario")
public class HomeUsuarioController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Empresas destacadas", description = "Devuelve una lista de empresas activas con el flag 'destacado' activado")
    @ApiResponse(responseCode = "200", description = "Listado de empresas exitoso")
    @GetMapping("/home")
    public ResponseEntity<List<EmpresaHomeUsuarioDto>> home() {
    	List<Empresa> empresas = empresaService.findByDestacadoYActivo(); 
    	
    	List<EmpresaHomeUsuarioDto> empresasDto = empresas.stream()
    		    .map(emp -> modelMapper.map(emp, EmpresaHomeUsuarioDto.class))
    		    .toList();

        return ResponseEntity.ok(empresasDto);
    }
}

