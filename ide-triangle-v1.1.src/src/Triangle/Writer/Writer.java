/*
  Kenny Vega 
  Se modifico para que generara el html en una carpeta respectiva
*/

package Triangle.Writer;

import Triangle.AbstractSyntaxTrees.Program;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    private String fileName;

    public Writer(String fileName) {
        this.fileName = fileName;
    }

    
    public void write(Program ast) {
    
        try {
            File dir = new File("SalidasArbol" + File.separator);
            dir.mkdirs();

            File xmlFile = new File(dir, fileName.concat(".xml"));
            FileWriter fileWriter = new FileWriter(xmlFile);

            fileWriter.write("<?xml version=\"1.0\" standalone=\"yes\"?>\n");
            WriterVisitor layout = new WriterVisitor(fileWriter);
            ast.visit(layout, null);

            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error while creating file for print the AST");
            e.printStackTrace();
        }
    }

}
