
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro Fernandez
 */
public class PicadorDeArchivo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
   JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));

    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".mp4")
            || f.isDirectory();
      }

      public String getDescription() {
        return "MP4 Videos";
      }
    });

    int r = chooser.showOpenDialog(new JFrame());
    String name = null;
    if (r == JFileChooser.APPROVE_OPTION) {
      name = chooser.getSelectedFile().getName();
      System.out.println(name);
    }
        String direccion =chooser.getCurrentDirectory() +"\\" +name;    
        File archivo = new File( direccion );    
        System.out.println("Direccion: "+ archivo);
        
        		FileInputStream fileInputStream = null;
		File file = new File(direccion);
		byte[] fileArray = new byte[(int) file.length()];

		try {
			// Con este código se obtienen los bytes del archivo.
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(fileArray);
			fileInputStream.close();

			// Con este código se agregan los bytes al archivo.
			FileOutputStream fileOuputStream = new FileOutputStream("C:\\prueba\\videoConvetido.mp4");
			fileOuputStream.write(fileArray);
			fileOuputStream.close();

		} catch (Exception e) {     
                    System.out.println(e.getMessage());
		}
            System.exit(0);
        }
        
    }
    
