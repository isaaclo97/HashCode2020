package structure;

import grafo.optilib.structure.Solution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HashCodeSolution implements Solution {

    List<LibrarySolution> librerias = new ArrayList<>();
    Set<Integer> librosElegidos = new HashSet<>();
    Set<Library> unusedLibraries = new HashSet<>();
    int currentDay = 0;

    private HashCodeInstance instance;

    public HashCodeSolution(HashCodeInstance instance) {
        this.instance = instance;
        unusedLibraries.addAll(Arrays.asList(instance.getLibraries()));
    }

    public HashCodeSolution(HashCodeSolution sol) {
        this.instance = sol.instance;
        this.unusedLibraries = new HashSet<>(sol.unusedLibraries);
        this.librerias = new ArrayList<>(sol.librerias);
        this.librosElegidos = new HashSet<>(sol.librosElegidos);
        this.currentDay = sol.currentDay;
    }


    public double getObjectiveFunctionValue() {
        librosElegidos.clear();
        for(LibrarySolution lib:librerias){
            for(Integer chossen: lib.getChosenBooks()){
                librosElegidos.add(chossen);
            }
        }
        long value = 0;
        int daysCounter = 0;
        Collections.sort(librerias, new Comparator<LibrarySolution>() {
            @Override
            public int compare(LibrarySolution o1, LibrarySolution o2) {
                return o1.getSubmitDay() - o2.getSubmitDay();
            }
        });
        Set<Integer> totalBooks = new HashSet<>();
        for (LibrarySolution libreria : librerias) {
            daysCounter+=libreria.instanceLibrary.signUpTime;
            int daysLibrary = 0;
            int cnt = 0;
            for (Integer libro : libreria.chosenBooks) {
                if(cnt%libreria.getInstanceLibrary().booksPerDay==0) daysLibrary++;
                if(daysCounter+daysLibrary>instance.days) break;
                cnt++;
                totalBooks.add(libro);
                value += instance.getBookScore(libro);
            }
            if(daysCounter>instance.days) break;
        }

        long value2 = 0;
        for (Integer libro : this.librosElegidos) {
            value2 += instance.getBookScore(libro);
        }
        System.out.println(value + " " + value2);
        if(value!=value2){
            System.out.println("Error");
        }
        //librosElegidos.removeAll(totalBooks);
        /*if (value2 != value) {
            throw new IllegalStateException("Panda subnormales");
        }*/
        return value;
    }

    public HashCodeInstance getInstance() {
        return instance;
    }

    public void shuffleList() {
        Collections.shuffle(librerias);
        int last = 0;
        for(LibrarySolution lib:librerias){
            lib.submitDay = last;
            last+=lib.instanceLibrary.signUpTime;
        }
        //DELETE RESTANTES FIRST
        for(LibrarySolution lib:librerias){
            if((lib.getSubmitDay()+lib.instanceLibrary.getSignUpTime())>=instance.days) {
                this.librosElegidos.removeAll(lib.getChosenBooks());
                lib.unusedBooks.addAll(lib.getChosenBooks());
                lib.chosenBooks.clear();
                continue;
            }
            int slots = (instance.getDays() - lib.instanceLibrary.signUpTime - lib.getSubmitDay())* lib.instanceLibrary.getBooksPerDay();
            if(slots<0){
                throw new IllegalStateException("Panda subnormales");
            }
            if(lib.getChosenBooks().size()>slots) {
                int sobrantes = Math.max(0,lib.getChosenBooks().size()-slots);
                ArrayList<Integer> ordered = new ArrayList<>(lib.getChosenBooks());
                Collections.sort(ordered);
                ordered = new ArrayList<>(ordered.subList(0,sobrantes));
                for(Integer e:ordered){
                    this.librosElegidos.remove(e);
                    lib.chosenBooks.remove(e);
                    lib.unusedBooks.add(e);
                }
            }
        }
        librosElegidos.clear();
        for(LibrarySolution lib:librerias){
            for(Integer chossen: lib.getChosenBooks()){
                librosElegidos.add(chossen);
            }
        }
        this.getObjectiveFunctionValue();
        //END DELETE RESTANTES
        //ADD NUEVOS
        for(LibrarySolution lib:librerias){
            int slots = (instance.getDays() - lib.instanceLibrary.signUpTime - lib.getSubmitDay())* lib.instanceLibrary.getBooksPerDay();
            if(slots<0){
                throw new IllegalStateException("Panda subnormales");
            }
            if(lib.unusedBooks.size()>0 && (slots-lib.getChosenBooks().size())>0){
                ArrayList<Integer> ordered = new ArrayList<>(lib.unusedBooks);
                Collections.sort(ordered, (a, b) -> -a.compareTo(b));
                int bookToAdd = Math.min(lib.unusedBooks.size(),slots-lib.getChosenBooks().size());
                for(int i=0; i<bookToAdd;i++){
                    int e = ordered.get(i);
                    if(this.librosElegidos.contains(e)) {
                        bookToAdd = Math.min(bookToAdd+1,lib.unusedBooks.size()); //Meter el siguiente
                        continue;
                    }
                    this.librosElegidos.add(e);
                    lib.chosenBooks.add(e);
                    lib.unusedBooks.remove(e);
                }
            }
        }
        librosElegidos.clear();
        for(LibrarySolution lib:librerias){
            for(Integer chossen: lib.getChosenBooks()){
                librosElegidos.add(chossen);
            }
        }
        this.getObjectiveFunctionValue();
        //END ADD NUEVOS
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

    public void writeSolution() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.instance.getName() + ".sol"))) {

            int total = 0;
            for (LibrarySolution librarySolution : librerias) {
                if (!librarySolution.chosenBooks.isEmpty()) {
                    total++;
                }
            }
            writer.append(String.format("%s\n", total));

            for (LibrarySolution librarySolution : librerias) {
                if (librarySolution.chosenBooks.isEmpty()) {
                    continue;
                }
                writer.append(String.format("%s %s\n", librarySolution.instanceLibrary.id, librarySolution.chosenBooks.size()));
                StringBuilder sb = new StringBuilder();
                for (Integer bookId : librarySolution.chosenBooks) {
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

    public void addLibrary(Library library) {
        this.librerias.add(new LibrarySolution(library, this.currentDay));
        this.unusedLibraries.remove(library);
        currentDay += library.getSignUpTime();
    }

    public boolean canAddLibrary(Library library) {
        return this.instance.getDays() - currentDay > library.getSignUpTime();
    }

    public Set<Library> getUnusedLibraries() {
        return unusedLibraries;
    }

    public List<LibrarySolution> getUsadasLibrerias() {
        return librerias;
    }

    public void sendBook(LibrarySolution library, int bookId) {
        if (library.chosenBooks.contains(bookId)) {
            throw new IllegalStateException("No deberia pasar nunca");
        }

        this.librosElegidos.add(bookId);
        library.chosenBooks.add(bookId);
        library.unusedBooks.remove(bookId);
    }

    public boolean isUsedBook(int book) {
        return librosElegidos.contains(book);
    }
}
