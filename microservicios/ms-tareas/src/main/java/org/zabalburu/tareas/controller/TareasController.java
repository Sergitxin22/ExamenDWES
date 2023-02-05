package org.zabalburu.tareas.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zabalburu.tareas.dto.TareaDTO;
import org.zabalburu.tareas.modelo.Tarea;
import org.zabalburu.tareas.service.TareasService;

@RestController
@RequestMapping("/tareas")
public class TareasController {
	@Autowired
	private TareasService servicio;
	
	@GetMapping("/{idUsuario}")
	public ResponseEntity<TareaDTO> getUsuarios(@RequestParam(defaultValue = "1") Integer pagina,
			@PathVariable Integer idUsuario){
		TareaDTO dto = servicio.getTareas(pagina, idUsuario);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(dto);
		}
	}
	
	@GetMapping("/pendientes/{idUsuario}")
	public ResponseEntity<TareaDTO> getTareasPendientes(@RequestParam(defaultValue = "1") Integer pagina,
			@PathVariable Integer idUsuario){
		TareaDTO dto = servicio.getTareasPendientes(pagina, idUsuario);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(dto);
		}
	}
	
	@PostMapping("/{idUsuario}")
	public ResponseEntity<Tarea> nuevaTarea(@PathVariable Integer idUsuario, @RequestBody Tarea t) throws URISyntaxException{
		t.setIdUsuario(idUsuario);
		t = servicio.nuevaTarea(t);
		if (t == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.created(new URI("http://localhost:9005/tareas/"+idUsuario+"/" +t.getId()))
					.body(t);
		}
	}
	
	@GetMapping("/{idUsuario}/{idTarea}")
	public ResponseEntity<Tarea> getUsuario(@PathVariable Integer idUsuario, @PathVariable Integer idTarea){
		Tarea t = servicio.getTarea(idUsuario,idTarea);
		if (t == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(t);
		}
	}
	
	@DeleteMapping("/{idUsuario}/{idTarea}")
	public ResponseEntity<?> eliminarTarea(@PathVariable Integer idUsuario, @PathVariable Integer idTarea){
		servicio.eliminarTarea(idUsuario,idTarea);
		return ResponseEntity.ok().build();
	}	
}
