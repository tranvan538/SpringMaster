package ems.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ems.model.Todo;
import ems.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {
    @Autowired
    private TodoService todoService;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping( value = "/list-todos", method = RequestMethod.GET )
    public String showTodoList( ModelMap model ) {
        model.addAttribute( "todos", todoService.getAll( getUsername() ) );
        return "list-todos";
    }

    @RequestMapping( value = "/add-todo", method = RequestMethod.GET )
    public String showAddTodoPage( ModelMap model ) {
        model.addAttribute("todo", new Todo());
        return "todo";
    }

    @RequestMapping( value = "/add-todo", method = RequestMethod.POST )
    public String addTodo( ModelMap model, @RequestParam(required=true) String desc,
                                           @RequestParam(required=true) String targetDate) {
        todoService.addTodo( getUsername(), desc, parseDateInput(targetDate), false );
        return "redirect:/list-todos";
    }

    @RequestMapping( value = "/update-todo", method = RequestMethod.GET )
    public String showUpdateTodoPage( ModelMap model, @RequestParam(required=true) int id) {
        model.addAttribute("todo", todoService.get(id));
        model.addAttribute("targetDate", dateFormat.format(todoService.get(id).getTargetDate()));
        return "todo";
    }

    @RequestMapping( value = "/update-todo", method = RequestMethod.POST )
    public String updateTodo( ModelMap model, @RequestParam(required=true) int id,
                                              @RequestParam(required=true) String desc,
                                              @RequestParam(required=true) String targetDate) {
        Todo todo = todoService.get(id);
        if(todo != null) {
            todo.setDesc( desc );
            todo.setTargetDate( parseDateInput(targetDate) );
        }
        return "redirect:/list-todos";
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( principal instanceof UserDetails ) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    private Date parseDateInput(String source) {
        if(source != null && !source.isEmpty()) {
            try {
                Date result = dateFormat.parse( source );
                return result;
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        return new Date();
    }
}
