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

    @Override
    public void readInstance(String path) {

        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance: " + this.name);
        FileReader fr= null;

        try (var reader = Files.newBufferedReader(Path.of(path))){
            // TODO reader code

        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public String getName() {
        return name;
    }
}
