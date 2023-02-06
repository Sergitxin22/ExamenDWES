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
	@Autowired
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
