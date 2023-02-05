package org.zabalburu.actividad15.dto;

import java.util.List;

import org.zabalburu.actividad15.modelo.Tarea;

import lombok.Data;

@Data
public class TareasDTO {
	private Integer pagina;
	private Integer totalPaginas;
	private Integer tareasPorPagina;
	private List<Tarea> tareas;
}
