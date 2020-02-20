package structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HashCodeInstance implements Instance {

    private String name;

    public HashCodeInstance(String path) {
        readInstance(path);
    }

    public void readInstance(String path) {

        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance: " + this.name);
        FileReader fr= null;
        int nodeCnt = 0;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br=new BufferedReader(fr);
        // read line by line
        String line;
        try{
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split("\t");

                index++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}
