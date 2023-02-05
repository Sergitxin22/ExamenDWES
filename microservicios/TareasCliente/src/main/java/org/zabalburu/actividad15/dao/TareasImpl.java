package org.zabalburu.actividad15.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.zabalburu.actividad15.dto.TareasDTO;
import org.zabalburu.actividad15.modelo.Tarea;
import org.zabalburu.actividad15.modelo.Usuario;

@Repository
public class TareasImpl implements TareasDAO {

	private static final String URL_USUARIOS = "http://localhost:8005/usuarios";
	private static final String URL_TAREAS = "http://localhost:9005/tareas";
	
	@Autowired
	private RestTemplate template;
	
	@Override
	public Usuario getUsuario(Integer id) {
		Usuario usu = null;
		try {
			usu = template.getForObject(URL_USUARIOS+"/{id}", Usuario.class, id);
		} catch (HttpClientErrorException.NotFound ex) {}
		return usu;
	}

	@Override
	public Usuario getUsuario(String usuario, String password) {
		Usuario usu = null;
		try {
			usu = template.getForObject(URL_USUARIOS+"/{usuario}/{password}", Usuario.class, usuario,password);
		} catch (HttpClientErrorException ex) {}
		return usu;
	}

	@Override
	public TareasDTO getTareas(Integer idUsuario, Integer pagina) {
		try {
			return template.getForObject(URL_TAREAS+"/{idUsuario}?pagina={pagina}", TareasDTO.class,idUsuario,pagina);
		} catch (HttpClientErrorException.NotFound ex) {
			return null;
		}
	}

	@Override
	public TareasDTO getTareasPendientes(Integer idUsuario) {
		try {
			return template.getForObject(URL_TAREAS+"/{idUsuario}/pendientes", TareasDTO.class,idUsuario);
		} catch (HttpClientErrorException.NotFound ex) {
			return null;
		}
	}

	@Override
	public Tarea nuevaTarea(Tarea t) {
		t = template.postForObject(URL_TAREAS+"/{idUsuario}", t, Tarea.class,t.getUsuario().getId());
		return t;
	}

	@Override
	public Tarea getTarea(Integer idUsuario, Integer idTarea) {
		Tarea t = null;
		try {
			t = template.getForObject(URL_TAREAS+"/{idUsuario}/{idTarea}", Tarea.class, idUsuario, idTarea);
		} catch (HttpClientErrorException ex) {
			
		}
		return t;
	}

	@Override
	public void eliminarTarea(Integer idUsuario, Integer idTarea) {
		template.delete(URL_TAREAS+"/{idUsuario}/{idTarea}",idUsuario, idTarea);
	}

}
