/*
 * ThreadCompress.java
 *
 */

package compresor;

import GUI.GestorVentanaPrincipal;
import java.io.File;

/**
 *
 * @author Liberal, Peker
 */

//Hilo para realizar la compresion
public class ThreadCompress extends Thread{
    private Compresor compresor;
    private File arch;
    private String nombre;
    private GestorVentanaPrincipal gestor;
    public boolean stopRequested=false; // parar o no el hilo de descompresion
    //private int count = 0;
public ThreadCompress( File f, GestorVentanaPrincipal gestor) {

        this.nombre= f.getName();
        compresor = new Compresor(gestor);
        arch = f;
        this.gestor = gestor;
    }

      @Override
    public void run() {
    while (!stopRequested) {
      //  try {
      //    Thread.sleep(300);
      //}
      //catch (InterruptedException x) {}
      //System.out.println("Running ... count=" + count);
      //count++;
      compresor.comprimirRecursivo(arch);
      stopRequested=true;
    }
  }

  public void stopRequest() { // Detener la descompresion recursiva
      compresor.setcomp(false);
  }
}
