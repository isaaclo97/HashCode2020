package structure;

import grafo.optilib.structure.Instance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HashCodeInstance implements Instance {

    private String name;
    int nBooks, nLibraries, days;
    int[] bookScore;
    Library[] libraries;

    public HashCodeInstance(String path) {
        readInstance(path);
    }

    public void readInstance(String path) {

        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance: " + this.name);
        try (Scanner sc = new Scanner(new FileInputStream(path))
        ) {

            // read line by line
            nBooks = sc.nextInt();
            nLibraries = sc.nextInt();
            days = sc.nextInt();

            bookScore = new int[nBooks];
            for (int i = 0; i < nBooks; i++) {
                bookScore[i] = sc.nextInt();
            }

            libraries = new Library[nLibraries];
            for (int i = 0; i < nLibraries; i++) {
                int nBooksLibrary = sc.nextInt();
                int signUpTime = sc.nextInt();
                int booksPerDay = sc.nextInt();

                long score = 0;
                Set<Integer> booksInLibrary = new HashSet<>();
                for (int j = 0; j < nBooksLibrary; j++) {
                    int book = sc.nextInt();
                    booksInLibrary.add(book);
                    score += bookScore[book];
                }

                libraries[i] = new Library(i, booksInLibrary, score, signUpTime, booksPerDay);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public int getnBooks() {
        return nBooks;
    }

    public int getnLibraries() {
        return nLibraries;
    }

    public int getDays() {
        return days;
    }

    public int getBookScore(int chosenBook) {
        return bookScore[chosenBook];
    }

    public Library[] getLibraries() {
        return libraries;
    }
}
