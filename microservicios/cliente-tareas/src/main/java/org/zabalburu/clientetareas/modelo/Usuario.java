package org.zabalburu.clientetareas.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

	@EqualsAndHashCode.Include
	private Integer id;
	
	private String nombre;	
	private String rol;	
	private String sexo;
}
