package structure;

import grafo.optilib.structure.Solution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HashCodeSolution implements Solution {

    List<LibrarySolution> librerias = new ArrayList<>();
    Set<Integer> librosElegidos = new HashSet<>();

    int currentDay = 0;
    //private double objValue;
    private HashCodeInstance instance;

    Set<Library> unusedLibraries = new HashSet<>();

    public HashCodeSolution(HashCodeInstance instance) {
        this.instance = instance;
        unusedLibraries.addAll(Arrays.asList(instance.getLibraries()));
    }


    public double getObjectiveFunctionValue() {
        long value = 0;
        for (LibrarySolution libreria : librerias) {
            for (Integer libro : libreria.chosenBooks) {
                value += instance.getBookScore(libro);
            }
        }
        long value2 = 0;
        for (Integer libro : this.librosElegidos) {
            value2 += instance.getBookScore(libro);
        }

        if(value2 != value){
            throw new IllegalStateException("Panda subnormales");
        }
        return value;
    }

    public HashCodeInstance getInstance() {
        return instance;
    }

    public static class LibrarySolution {
        Library instanceLibrary;
        int submitDay;
        Set<Integer> chosenBooks = new HashSet<>();
        Set<Integer> unusedBooks = new HashSet<>();

        public LibrarySolution(Library instanceLibrary, int submitDay) {
            this.instanceLibrary = instanceLibrary;
            this.unusedBooks.addAll(instanceLibrary.getBooks());
            this.submitDay = submitDay;
        }

        public Library getInstanceLibrary() {
            return instanceLibrary;
        }

        public int getSubmitDay() {
            return submitDay;
        }

        public Set<Integer> getChosenBooks() {
            return chosenBooks;
        }

        public Set<Integer> getUnusedBooks() {
            return unusedBooks;
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

    public List<LibrarySolution> getUsadasLibrerias() {
        return librerias;
    }

    public void sendBook(LibrarySolution library, int bookId){
        if(library.chosenBooks.contains(bookId)){
            throw new IllegalStateException("No deberia pasar nunca");
        }

        this.librosElegidos.add(bookId);
        library.chosenBooks.add(bookId);
        library.unusedBooks.remove(bookId);
    }

    public boolean isUsedBook(int book){
        return librosElegidos.contains(book);
    }
}
