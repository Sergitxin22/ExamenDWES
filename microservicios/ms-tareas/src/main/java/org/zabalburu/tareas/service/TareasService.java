package org.zabalburu.tareas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.zabalburu.tareas.dao.TareasRepository;
import org.zabalburu.tareas.dto.TareaDTO;
import org.zabalburu.tareas.modelo.Tarea;

@Service
public class TareasService {
	@Autowired
	private TareasRepository dao;
	
	@Autowired
	private RestTemplate template;
	
	private boolean existeUsuario(Integer idUsuario) {
		try {
			template.getForObject("http://localhost:8005/usuarios/{id}", Object.class, idUsuario);
			return true;
		} catch (HttpClientErrorException.NotFound ex) {
			return false;
		}
	}
	
	public TareaDTO getTareas(Integer pagina, Integer idUsuario) {
		if (existeUsuario(idUsuario)) {
			Pageable pg = PageRequest.of(pagina-1, 3);
			Page<Tarea> page = dao.findByIdUsuarioOrderByFechaDesc(pg, idUsuario);
			TareaDTO dto = new TareaDTO();
			dto.setPagina(pagina);
			dto.setTareas(page.getContent());
			dto.setTareasPorPagina(3);
			dto.setTotalPaginas(page.getTotalPages());
			return dto;
		} else {
			return null;
		}
	}
	
	public TareaDTO getTareasPendientes(Integer pagina, Integer idUsuario) {
		if (existeUsuario(idUsuario)) {
			Pageable pg = PageRequest.of(pagina-1, 3);
			Page<Tarea> page = dao.getTareasPendientes(pg, idUsuario);
			TareaDTO dto = new TareaDTO();
			dto.setPagina(pagina);
			dto.setTareas(page.getContent());
			dto.setTareasPorPagina(3);
			dto.setTotalPaginas(page.getTotalPages());
			return dto;
		} else {
			return null;
		}
	}
	
	public Tarea nuevaTarea(Tarea t) {
		if (existeUsuario(t.getIdUsuario())) {
			return dao.save(t);
		} else {
			return null;
		}
	}
	
	public Tarea getTarea(Integer idUsuario, Integer idTarea) {
		Tarea t = null;
		if (existeUsuario(idUsuario)) {
			t = dao.findByIdUsuarioAndId(idUsuario, idTarea).orElse(null);
		}
		return t;
	}
	
	public void eliminarTarea(Integer idUsuario, Integer idTarea) {
		try {
			dao.delete(getTarea(idUsuario, idTarea));
		} catch (Exception ex) {}
	}
}
