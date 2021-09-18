/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hp
 */
import java.util.Scanner;
import java.io.Serializable; 

public class InvertirCadena  {
    public static void main(String[] args) {
	  String cadena = "";
 
      //Si se ha pasado la cadena como parámetro al ejecutable
      if (args.length == 1) {
	  cadena = args[0];
      }
      //Si no se ha pasado la cadena como parámetro la solicitamos al usuario por consola
      if (cadena.equals("")) {
          Scanner lecturaTeclado = new Scanner(System.in);
          System.out.print("Introduzca una cadena de texto y pulse INTRO: ");
          cadena = lecturaTeclado.nextLine();
      }
      if (!cadena.isEmpty()) {
          System.out.print("La cadena \"" + cadena + "\" invertida es: " + InvertirCadena(cadena));
      } else {
          System.out.println("No ha introducido una cadena de texto.");
      }
    }
 
    //Método al que se le pasa como parámetro una cadena y la devuelve invertida
    public static String InvertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x-- )
            cadenaInvertida = cadenaInvertida + cadena.charAt(x) +" ";
        return cadenaInvertida;
    }
}
