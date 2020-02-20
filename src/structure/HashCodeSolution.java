package structure;

import grafo.optilib.structure.Solution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashCodeSolution implements Solution {

    List<LibrarySolution> librerias = new ArrayList<>();
    int remainingDays;
    private Double mark;
    private HashCodeInstance instance;

    public HashCodeSolution(HashCodeInstance instance) {
        this.instance = instance;
        this.remainingDays = instance.days;
        for (Library lib : instance.libraries) {
            this.librerias.add(new LibrarySolution(lib));
        }
    }

    public Double getMark(){
        return this.mark;
    }

    public double getObjectiveFunctionValue() {
        if(this.mark==null){
            return this.mark = getMark();
        }
        return this.mark;
    }

    public HashCodeInstance getInstance() {
        return instance;
    }

    private static class LibrarySolution {
        Library instanceLibrary;
        int submitDay;
        Set<Integer> chosenBooks = new HashSet<>();

        public LibrarySolution(Library instanceLibrary) {
            this.instanceLibrary = instanceLibrary;
        }
    }

    public void writeSolution(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.instance.getName() + ".sol"))) {
            writer.append(String.format("%s\n", this.librerias.size()));
            for(LibrarySolution librarySolution: librerias){
                // todo estoy aqui
                writer.append(String.format("%s %s\n",));

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
