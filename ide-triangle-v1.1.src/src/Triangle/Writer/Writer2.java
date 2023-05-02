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
            //create html file
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
   
    public void writeReservedWord(String word){
        try {
            writeHTML("<b> " + word + " </b>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write(String word){
        try {
            writeHTML("<span style=\"color:black\">" + word + "</span>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeComment(String word){
        try {
            writeHTML("<span style=\"color:green\">" + word + "</span><br>");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeColor(String word){
        try {
            writeHTML("<span style=\"color:blue\">" + word + "</span>");
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

    public void writeTab(){
        try {
            writeHTML("&nbsp;&nbsp;&nbsp;");
        } catch (IOException ex) {
            Logger.getLogger(Writer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
