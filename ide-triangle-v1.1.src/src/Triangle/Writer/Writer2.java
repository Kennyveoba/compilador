/*
  Kenny Vega

  Se agregaron las clases para crear el html con su respectivo formato 
*/

package Triangle.Writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Writer2 {
    private FileWriter fileWriter;

    public Writer2(String fileName){
        try {
            
            File carpeta = new File("SalidasHTML" + File.separator);
            carpeta.mkdirs();
            File htmlFile = new File(carpeta,fileName.concat(".html"));
       
            fileWriter = new FileWriter(htmlFile);
            writeHTML("<p style=\"font-family: monospace; font-size: 1em;\">");
        
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeHTML(String html) throws IOException {
        fileWriter.write(html);
        fileWriter.flush();
    }
    
    
    public void write(String texto){
        try {
            writeHTML("<span style=\"color:black\">" + texto + "</span>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TerminarHTML(){
        try {
            writeHTML("</p>");
            writeHTML("\n");
            writeHTML("</html>");
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   

    public void writeComment(String texto){
        try {
            writeHTML("<span style=\"color:green\">" + texto + "</span><br>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeReservedWord(String texto){
        try {
            writeHTML("<b> " + texto + " </b>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeTab(){
        try {
            writeHTML("&nbsp;&nbsp;&nbsp;");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void writeColor(String texto){
        try {
            writeHTML("<span style=\"color:blue\">" + texto + "</span>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeEndOfLine(){
        try {
            writeHTML("<br>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}