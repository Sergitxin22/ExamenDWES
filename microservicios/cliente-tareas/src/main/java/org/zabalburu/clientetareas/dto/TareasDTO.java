package org.zabalburu.clientetareas.dto;

import java.util.List;

import org.zabalburu.clientetareas.modelo.Tarea;

import lombok.Data;

@Data
public class TareasDTO {
	private Integer pagina;
	private Integer totalPaginas;
	private Integer tareasPorPagina;
	private List<Tarea> tareas;
}
