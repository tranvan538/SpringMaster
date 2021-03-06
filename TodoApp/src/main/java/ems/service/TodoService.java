package ems.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ems.model.Todo;

import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<Todo>();
    private static int todoCount = 3;

    static {
        todos.add( new Todo( 1, "tvdinh", "Learn Spring MVC", new Date(), false ) );
        todos.add( new Todo( 2, "tvdinh", "Learn Strut", new Date(), false ) );
        todos.add( new Todo( 3, "tvdinh", "Learn Hibernate", new Date(), false ) );
    }

    public List<Todo> getAll() {
        return todos;
    }

    public List<Todo> getAll( String user ) {
        List<Todo> result = new ArrayList<Todo>();
        if ( user != null ) {
            for ( Todo todo : todos ) {
                if ( user.equals( todo.getUser() ) ) {
                    result.add( todo );
                }
            }
        }
        return result;
    }

    public void addTodo( Todo todo ) {
        todos.add( todo );
    }

    public void addTodo( String username, String desc, Date parseDateInput, boolean done ) {
        todos.add( new Todo(++todoCount, username, desc, parseDateInput, done) );
    }

    public boolean remove(int id) {
        Iterator<Todo> iterator = todos.iterator();
        while(iterator.hasNext()) {
            Todo todo = iterator.next();
            if(id == todo.getId()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Todo get( int id ) {
        for ( Todo todo : todos ) {
            if ( todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }
}
