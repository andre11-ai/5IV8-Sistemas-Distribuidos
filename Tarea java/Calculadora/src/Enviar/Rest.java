/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enviar;

/**
 *
 * @author Braulio Barrientos C
 */
public class Rest {
    private Cons cima;
    private int n;
    public Rest(){
        cima=null;
    }
    public boolean esVacia(){
        return cima==null;
    }
    public void apilar(Object item){
        Cons nuevoNodo=new Cons(item);
        if(esVacia()) cima=nuevoNodo;
        else{
            nuevoNodo.setSgteNodo(cima);
            cima=nuevoNodo;
        }n++;
    }
    public Object desapilar(){
        Object Aux=cima.getItem(); //toma a aquel item que vamos a desapilar
        if(esVacia()) return null;
        else{
            cima=cima.getSgteNodo();
            n--;
            return Aux;
        }
    }
    public Object ultimo(){
        if (esVacia()) return null;
        else return cima.getItem();
    }
    public int longitud(){
        return n;
    }
}
