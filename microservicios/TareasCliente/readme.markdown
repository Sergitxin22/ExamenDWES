## CREAR CLIENTE - CLIENTE-TAREAS
1. [Creación del proyecto](#creación-del-proyecto)
2. [Configuración del proyecto](#configuración-del-proyecto)
3. [Creación de los modelos](#creación-de-los-modelos)
4. [Creación del DTO](#creación-del-dto)
5. [Creación de los DAOs](#creación-de-los-daos)
6. [Creación del controlador](#creación-del-controlador)
7. [Crear páginas jsp](#crear-páginas-jsp)
### Creación del proyecto
***
1. File → New → Spring Starter Project
2. [Primera pantalla](/microservicios/images/creacion-proyecto-1.PNG)
3. [Segunda pantalla](/microservicios/images/librerias-cliente.PNG)
### Configuración del proyecto
***
Para utilizar posteriormente el RestTemplate añadimos al archivo ClienteTareasApplication el siguiente Bean:
```
package org.zabalburu.clientetareas;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClienteTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteTareasApplication.class, args);
	}
	
	@Bean
	public RestTemplate getTemplate() {
		return new RestTemplate();
	}
}
```
Para mostrar páginas jsp añadimos las siguientes dependencias al archivo pom.xml:
```
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
</dependency>
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>
```
Una vez creado el proyecto vamos a configurar el fichero application.properties ( en src → main → resources) para asignar un puerto a la aplicación y decirle donde debe buscar las páginas .jsp:

```
spring.mvc.view.prefix=/vistas/
spring.mvc.view.suffix=.jsp
server.port=8000
```
Creamos la carpeta src/main/webapp/vistas/ donde posteriormente crearemos las páginas .jsp
### Creación de los modelos
***
Creamos el modelo Usuario.class en el paquete org.zabalburu.clientetareas.modelo
```
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
```
Creamos el modelo Tarea.class en el paquete org.zabalburu.clientetareas.modelo
```
package org.zabalburu.clientetareas.modelo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tarea {

	@EqualsAndHashCode.Include
	private Integer id;
	
	private Usuario usuario;
	
	@NotBlank(message = "Debe especificarse el título de la tarea")
	private String titulo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;
	
	private String descripcion;	
	private Boolean realizada;	
}
```
### Creación del DTO
***
Para incluir la paginación, creamos el DTO TareasDTO.class en el paquete org.zabalburu.clientetareas.dto
```
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
```
### Creación de los DAOs
***
Primero creamos la interfaz TareasDAO en el paquete org.zabalburu.clientetareas.dao para definir los métodos de la aplicación:
```
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
```
Ahora creamos la clase TareasImpl.java en el paquete org.zabalburu.clientetareas.dao, en la que implementamos la interfaz anterior:
```
package org.zabalburu.clientetareas.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.zabalburu.clientetareas.dto.TareasDTO;
import org.zabalburu.clientetareas.modelo.Tarea;
import org.zabalburu.clientetareas.modelo.Usuario;

public class TareasImpl implements TareasDAO {
	
	private static final String URL_USUARIOS = "http://localhost:8005/usuarios";
	private static final String URL_TAREAS = "http://localhost:9005/tareas";
	
	@Autowired
	private RestTemplate template;
	
	@Override
	public Usuario getUsuario(Integer id) {
		Usuario usu = null;
		try {
			usu = template.getForObject(URL_USUARIOS + "/{id}", Usuario.class, id);
		} catch (HttpClientErrorException.NotFound ex) {}
		return usu;
	}

	@Override
	public Usuario getUsuario(String usuario, String password) {
		Usuario usu = null;
		try {
			usu = template.getForObject(URL_USUARIOS + "/{usuario}/{password}", Usuario.class, usuario,password);
		} catch (HttpClientErrorException ex) {}
		return usu;
	}

	@Override
	public TareasDTO getTareas(Integer idUsuario, Integer pagina) {
		try {
			return template.getForObject(URL_TAREAS + "/{idUsuario}?pagina={pagina}", TareasDTO.class,idUsuario,pagina);
		} catch (HttpClientErrorException.NotFound ex) {
			return null;
		}
	}

	@Override
	public TareasDTO getTareasPendientes(Integer idUsuario) {
		try {
			return template.getForObject(URL_TAREAS + "/{idUsuario}/pendientes", TareasDTO.class,idUsuario);
		} catch (HttpClientErrorException.NotFound ex) {
			return null;
		}
	}

	@Override
	public Tarea nuevaTarea(Tarea t) {
		t = template.postForObject(URL_TAREAS + "/{idUsuario}", t, Tarea.class,t.getUsuario().getId());
		return t;
	}

	@Override
	public Tarea getTarea(Integer idUsuario, Integer idTarea) {
		Tarea t = null;
		try {
			t = template.getForObject(URL_TAREAS + "/{idUsuario}/{idTarea}", Tarea.class, idUsuario, idTarea);
		} catch (HttpClientErrorException ex) {
			
		}
		return t;
	}

	@Override
	public void eliminarTarea(Integer idUsuario, Integer idTarea) {
		template.delete(URL_TAREAS + "/{idUsuario}/{idTarea}",idUsuario, idTarea);
	}
}
```
### Creación del controlador
***
Ahora creamos la clase TareasController en el paquete org.zabalburu.clientetareas.controlador y le ponemos el decorador @Controller, también inyectamos el dao con el decorador @Autowired
```
package org.zabalburu.clientetareas.controlador;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.zabalburu.clientetareas.dao.TareasDAO;
import org.zabalburu.clientetareas.modelo.Tarea;
import org.zabalburu.clientetareas.modelo.Usuario;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"usuario"})
public class TareasController {	
	private TareasDAO dao;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam String usuario, @RequestParam String password,
			Model modelo) {
		Usuario user = dao.getUsuario(usuario, password);
		Tarea t = new Tarea();
		t.setFecha(new Date());
		if (user == null) {
			modelo.addAttribute("error","Usuario / password erróneos");
			return "index";
		}
		modelo.addAttribute("usuario",user);
		modelo.addAttribute("tareas", dao.getTareas(user.getId(),1));
		modelo.addAttribute("tarea",t);
		return "usuario";
	}
	
	@RequestMapping("/nuevaTarea")
	public String nuevaTarea(@Valid @ModelAttribute Tarea t,
			BindingResult result, Model modelo) {
		Usuario usuario = (Usuario) modelo.getAttribute("usuario");
		if (result.hasErrors()) {
			modelo.addAttribute("tareas", dao.getTareas(usuario.getId(),1));
			return "usuario";
		}
		t.setUsuario(usuario);
		t.setRealizada(false);
		t = dao.nuevaTarea(t);
		modelo.addAttribute("tareas", dao.getTareas(usuario.getId(),1));
		t = new Tarea();
		t.setFecha(new Date());
		modelo.addAttribute("tarea",t);
		modelo.addAttribute("msg","Tarea añadida con éxito!");
		return "usuario";
	}
	
	@RequestMapping("/finalizar")
	public String finalizarTarea(@RequestParam Integer idTarea, Model modelo) {
		Usuario usuario = (Usuario) modelo.getAttribute("usuario");
		Tarea t = dao.getTarea(usuario.getId(), idTarea);
		if (t!=null) {
			t.setRealizada(true);
			dao.nuevaTarea(t);
		}
		modelo.addAttribute("tareas", dao.getTareas(usuario.getId(),1));
		t = new Tarea();
		t.setFecha(new Date());
		modelo.addAttribute("tarea",t);
		modelo.addAttribute("msg","Tarea finalizada con éxito!");
		return "usuario";
	}
	
	@RequestMapping("/eliminar")
	public String eliminarTarea(@RequestParam Integer idTarea, Model modelo) {
		Usuario usuario = (Usuario) modelo.getAttribute("usuario");
		dao.eliminarTarea(usuario.getId(), idTarea);
		modelo.addAttribute("tareas", dao.getTareas(usuario.getId(),1));
		Tarea t = new Tarea();
		t.setFecha(new Date());
		modelo.addAttribute("tarea",t);
		modelo.addAttribute("msg","Tarea eliminada con éxito!");
		return "usuario";
	}
	
	@RequestMapping("/ir")
	public String ir(@RequestParam Integer pos, Model modelo) {
		Usuario usuario = (Usuario) modelo.getAttribute("usuario");
		modelo.addAttribute("tareas", dao.getTareas(usuario.getId(),pos));
		Tarea t = new Tarea();
		t.setFecha(new Date());
		modelo.addAttribute("tarea",t);
		return "usuario";
	}
	
	@RequestMapping("/salir")
	public String salir(HttpSession sesion) {
		sesion.invalidate();
		return "index";
	}
}
```
### Crear páginas jsp
***
Para finalizar hay que crear las páginas .jsp\
index.jsp:
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Gestión Tareas</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8 text-center">
				<h1>Identifíquese ${usuario }</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8">

				<form action="/login" method="post">
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Usuario</label>
						<input type="text" class="form-control" id="exampleInputEmail1"
							aria-describedby="emailHelp" name="usuario" value="${param.usuario }">
					</div>
					<div class="mb-3">
						<label for="exampleInputPassword1" class="form-label">Password</label>
						<input type="password" class="form-control"
							id="exampleInputPassword1" name="password">
					</div>
					<button type="submit" class="btn btn-primary">Submit</button>
				</form>
			</div>
		</div>
		<c:if test="${!empty error }">
			<div class="row mt-3">
				<div class="col-2"></div>
				<div class="col-8 text-center">
					<div class="alert alert-danger">${error }</div>
				</div>
			</div>
		</c:if>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
		crossorigin="anonymous"></script>
</body>
</html>
```
usuario.jsp:
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="frm" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Gestión Tareas</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8 text-center">
				<h1>
					Bienvenido/a ${usuario.nombre } <a href="/salir"
						class="btn btn-outline-primary">Salir</a>
				</h1>
			</div>
		</div>
		<c:if test="${! empty msg }">
			<div class="row">
				<div class="col-2"></div>
				<div class="col-8">
					<div class="alert alert-success">${msg }</div>
				</div>
			</div>
		</c:if>
		<c:if test="${empty tareas.tareas }">
			<div class="row">
				<div class="col-2"></div>
				<div class="col-8">
					<div class="alert alert-info">No hay tareas</div>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty tareas.tareas }">
			<div class="row">
				<div class="col-12">
					<table class="table">
						<thead>
							<tr>
								<th scope="col">#</th>
								<th scope="col">Título</th>
								<th scope="col">Fecha</th>
								<th scope="col">Descripción</th>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="t" items="${tareas.tareas }">
								<tr>
									<th scope="row">${t.id }</th>
									<td>${t.titulo}</td>
									<td><fmt:formatDate value="${t.fecha }" /></td>
									<td>${t.descripcion }</td>
									<td><c:if test="${!t.realizada }">
											<a href="/finalizar?idTarea=${t.id }"
												class="btn btn-outline-primary">Finalizar</a>
										</c:if></td>
									<td><a href="/eliminar?idTarea=${t.id }"
										class="btn btn-outline-danger">Eliminar</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<nav aria-label="Page navigation example">
					<ul class="pagination">
						<c:if test="${tareas.pagina > 1 }">
							<li class="page-item"><a class="page-link"
								href="/ir?pos=${tareas.pagina-1 }">Anterior</a></li>
						</c:if>
						<c:forEach begin="1" end="${tareas.totalPaginas }" var="pos">
						<li class="page-item"><a class="page-link"
							href="/ir?pos=${pos }">${pos }</a></li>
						</c:forEach>
						
						<c:if test="${tareas.pagina <tareas.totalPaginas }">
							<li class="page-item"><a class="page-link"
								href="/ir?pos=${tareas.pagina+1 }">Siguiente</a></li>
						</c:if>
					</ul>
				</nav>
			</div>
		</c:if>
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8">
				<frm:form action="/nuevaTarea" modelAttribute="tarea">
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Título</label>
						<frm:input type="text" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp"
							path="titulo" />
						<frm:errors path="titulo" cssClass="text-danger"></frm:errors>
					</div>
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Fecha</label>
						<frm:input type="date" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp" path="fecha" />
					</div>
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Descripción</label>
						<frm:textarea type="text" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp"
							path="descripcion" />
					</div>
					<button type="submit" class="btn btn-primary">Guardar</button>
				</frm:form>

			</div>
		</div>
		<c:if test="${!empty error }">
			<div class="row mt-3">
				<div class="col-2"></div>
				<div class="col-8 text-center">
					<div class="alert alert-danger">${error }</div>
				</div>
			</div>
		</c:if>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
		crossorigin="anonymous"></script>
</body>
</html>
```
