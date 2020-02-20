package structure;

import java.util.Set;

public class Library {

    private Set<Integer> books;
    long value;
    int id;
    int signUpTime;
    int booksPerDay;

    public Library(int id, Set<Integer> books, long value, int signUpTime, int booksPerDay) {
        this.id = id;
        this.books = books;
        this.value = value;
        this.signUpTime = signUpTime;
        this.booksPerDay = booksPerDay;
    }
}
