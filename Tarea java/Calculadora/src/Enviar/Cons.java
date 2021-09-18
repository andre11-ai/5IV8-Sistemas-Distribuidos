/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enviar;
import java.io.Serializable;

/**
 *
 * @author Braulio Barrientos C
 */
public class Cons {
    
    private Object item;
    private Cons sgteNodo;
    public Cons(){
        item=null;
    }
    public Cons(Object item){
        this.item=item;
    }
    public Cons(Object item, Cons sgteNodo){
        this.item=item;
        this.sgteNodo=sgteNodo;
    }
    public Object getItem() {
        return item;
    }
    public void setItem(Object item) {
        this.item = item;
    }
    public Cons getSgteNodo() {
        return sgteNodo;
    }
    public void setSgteNodo(Cons sgteNodo) {
        this.sgteNodo = sgteNodo;
    }
    @Override
    public String toString() {
        return item.toString();
    }
    
    
}
