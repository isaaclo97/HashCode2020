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
        this.unusedLibraries = new HashSet<>();
        for(Library e:sol.getUnusedLibraries()){
            this.unusedLibraries.add(e);
        }
        this.librerias = new ArrayList<>();
        for(LibrarySolution e:sol.librerias){
            LibrarySolution aux = new LibrarySolution(e);
            this.librerias.add(aux);
        }
        this.librosElegidos = new HashSet<>();
        for(Integer e: sol.librosElegidos){
            this.librosElegidos.add(e);
        }
        this.currentDay = sol.currentDay;
    }


    public double getObjectiveFunctionValue() {
        librosElegidos.clear();
        for(LibrarySolution lib:librerias){
            for(Integer chossen: lib.getChosenBooks()){
                librosElegidos.add(chossen);
            }
        }
        if(librosElegidos.size()==0){
            throw new IllegalStateException("Panda subnormales");
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
        //System.out.println(value + " " + value2);
        //librosElegidos.removeAll(totalBooks);
        if (value2 != value) {
            throw new IllegalStateException("Panda subnormales");
        }
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
        //END DELETE RESTANTES

        //ADD NUEVOS
        for(LibrarySolution lib:librerias){
            lib.unusedBooks.removeAll(this.librosElegidos);
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
                    this.librosElegidos.add(e);
                    lib.chosenBooks.add(e);
                    lib.unusedBooks.remove(e);
                }
            }
        }
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

        public LibrarySolution(LibrarySolution e) {
            this.instanceLibrary = e.instanceLibrary;
            this.submitDay = e.submitDay;
            this.chosenBooks = new HashSet<>();
            for(Integer elem: e.chosenBooks){
                this.chosenBooks.add(elem);
            }
            this.unusedBooks = new HashSet<>();
            for(Integer elem: e.unusedBooks){
                this.unusedBooks.add(elem);
            }
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LibrarySolution)) return false;
            LibrarySolution that = (LibrarySolution) o;
            return submitDay == that.submitDay &&
                    Objects.equals(instanceLibrary, that.instanceLibrary) &&
                    Objects.equals(chosenBooks, that.chosenBooks) &&
                    Objects.equals(unusedBooks, that.unusedBooks);
        }

        @Override
        public int hashCode() {
            return Objects.hash(instanceLibrary, submitDay, chosenBooks, unusedBooks);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashCodeSolution)) return false;
        HashCodeSolution that = (HashCodeSolution) o;
        return currentDay == that.currentDay &&
                Objects.equals(librerias, that.librerias) &&
                Objects.equals(librosElegidos, that.librosElegidos) &&
                Objects.equals(unusedLibraries, that.unusedLibraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(librerias, librosElegidos, unusedLibraries, currentDay);
    }
}
