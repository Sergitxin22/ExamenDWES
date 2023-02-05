package org.zabalburu.actividad15.dao;

import java.util.List;

import org.zabalburu.actividad15.dto.TareasDTO;
import org.zabalburu.actividad15.modelo.Tarea;
import org.zabalburu.actividad15.modelo.Usuario;

public interface TareasDAO {
	public Usuario getUsuario(Integer id);
	public Usuario getUsuario(String usuario, String password);
	public TareasDTO getTareas(Integer idEmpleado, Integer pagina);
	public TareasDTO getTareasPendientes(Integer idEmpleado);
	public Tarea nuevaTarea(Tarea t);
	public Tarea getTarea(Integer idUsuario, Integer idTarea);
	public void eliminarTarea(Integer idUsuario, Integer idTarea);

}
