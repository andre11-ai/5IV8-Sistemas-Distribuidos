/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Funcionesarchivo {
    
    private File archivo=new File("datos.dat");
    private static ObjectInputStream entrada;
    private static ObjectOutputStream salida;
    
    ArrayList<Persona> per= new ArrayList();
    
    public void Escribir() throws FileNotFoundException, IOException{
        
        ObjectOutputStream salida;
        
        FileOutputStream fileoutput= new FileOutputStream(archivo);
        salida =new ObjectOutputStream(fileoutput);
        salida.writeObject(per);
        
        salida.close();
    }
    
    
    public void Leer() throws FileNotFoundException, IOException, ClassNotFoundException{
        
        FileInputStream fileinput= new FileInputStream(archivo);
        ObjectInputStream entrada=new ObjectInputStream(fileinput);
        
        per=(ArrayList)entrada.readObject();
        
        for(Persona p:per){
            
            
            JOptionPane.showMessageDialog(null,"============================\n"
            
                       +"Nombr: "+p.getNombre()+"\n"
                       +"Apellido Paterno:"+p.getAmaterno()+"\n"
                       +"Apellido Materno: "+p.getAmaterno()+"\n"
                       +"Edad"+p.getEdad()+"\n"
                                  
            );
            
            
        }
        
        
    }
    
    public void agregarPersona(String nombre,String paterno, String materno, int edad) throws IOException{
        Persona obj= new Persona(nombre,paterno,materno,edad);
        per.add(obj);
        Escribir();
        JOptionPane.showMessageDialog(null,"La persona ha sido agregada");
        
    }
    
}
