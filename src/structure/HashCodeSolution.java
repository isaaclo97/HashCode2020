package structure;

import grafo.optilib.structure.Solution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HashCodeSolution implements Solution {

    List<LibrarySolution> librerias = new ArrayList<>();
    int currentDay = 0;
    private Double mark;
    private HashCodeInstance instance;

    Set<Library> unusedLibraries = new HashSet<>();

    public HashCodeSolution(HashCodeInstance instance) {
        this.instance = instance;
        unusedLibraries.addAll(Arrays.asList(instance.getLibraries()));
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

        public LibrarySolution(Library instanceLibrary, int submitDay) {
            this.instanceLibrary = instanceLibrary;
            this.submitDay = submitDay;
        }
    }

    public void writeSolution(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.instance.getName() + ".sol"))) {
            writer.append(String.format("%s\n", this.librerias.size()));
            for(LibrarySolution librarySolution: librerias){
                writer.append(String.format("%s %s\n", librarySolution.instanceLibrary.id, librarySolution.chosenBooks.size()));
                StringBuilder sb = new StringBuilder();
                for(Integer bookId: librarySolution.chosenBooks){
                    sb.append(bookId).append(" ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
                writer.append(sb);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLibrary(Library library){
        this.librerias.add(new LibrarySolution(library, this.currentDay));
        currentDay += library.getSignUpTime();
    }

    public boolean canAddLibrary(Library library){
        return this.instance.getDays() - currentDay > library.getSignUpTime();
    }

    public Set<Library> getUnusedLibraries() {
        return unusedLibraries;
    }
}
