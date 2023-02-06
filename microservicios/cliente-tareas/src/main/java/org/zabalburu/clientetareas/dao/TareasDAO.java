package org.zabalburu.clientetareas.dao;

import org.zabalburu.clientetareas.dto.TareasDTO;
import org.zabalburu.clientetareas.modelo.Tarea;
import org.zabalburu.clientetareas.modelo.Usuario;

public interface TareasDAO {
	public Usuario getUsuario(Integer id);
	public Usuario getUsuario(String usuario, String password);
	public TareasDTO getTareas(Integer idEmpleado, Integer pagina);
	public TareasDTO getTareasPendientes(Integer idEmpleado);
	public Tarea nuevaTarea(Tarea t);
	public Tarea getTarea(Integer idUsuario, Integer idTarea);
	public void eliminarTarea(Integer idUsuario, Integer idTarea);
}
