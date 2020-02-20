import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main20M {

    public static void main(String[] args) throws Exception{
        String[] names = {"b_read_on.txt","c_incunabula.txt","d_tough_choices.txt","e_so_many_books.txt","f_libraries_of_the_world.txt"};
        String[] outs = {"b","c","d","e","f"};
        for (int i = 0; i < names.length; i++) {
            sovle(names[i],outs[i]);
        }
    }

    public static void sovle(String in,String out) throws IOException {
        String filenameIn = "instances/"+in;
        String filenameOut = out+".out";
        Scanner sc = new Scanner(new File(filenameIn));
        PrintWriter pw = new PrintWriter(new File(filenameOut));

        int bookNum = sc.nextInt();
        int storeNum = sc.nextInt();
        int deadline = sc.nextInt();
        int[] scores = new int[bookNum+1];
        for (int i = 0; i < bookNum; i++) {
            scores[0] = sc.nextInt();
        }
        HashMap<Integer,BookStore> map = new HashMap<>(storeNum);
        ArrayList<BookStore> stores = new ArrayList<>(storeNum);
        for (int i = 0; i < storeNum; i++) {
            int booksInStore = sc.nextInt();
            int signup = sc.nextInt();
            int booksPerDay = sc.nextInt();
            BookStore bs = new BookStore(i,booksInStore,signup,booksPerDay);
            stores.add(bs);
            map.put(i,bs);
            for (int j = 0; j < booksInStore; j++) {
                bs.addBook(sc.nextInt());
            }
            Collections.sort(bs.booksList,(b1, b2) -> Integer.compare(scores[b1],scores[b2]));
        }

        Collections.sort(stores,(bs1, bs2) -> Integer.compare(bs1.signupTime,bs2.signupTime));


        ArrayList<Integer> bookstoresSol = new ArrayList<>();
        int pos = 0;
        for (int day = 0; day < deadline;) {
            if (pos >= stores.size())break;
            BookStore cur = stores.get(pos);
            pos++;
            day = day + cur.signupTime;
            if (day<deadline){
                bookstoresSol.add(cur.id);
                bookstoresSol.add(day - cur.signupTime);
            }
        }

        HashSet<Integer> usedBooks = new HashSet<>();
        pw.println(bookstoresSol.size()/2);
        for (int i = 0; i < bookstoresSol.size(); i+=2) {
            ArrayList<Integer> solAux = new ArrayList<>();
            BookStore bs = map.get(bookstoresSol.get(i));
            int readMax =  (deadline-bookstoresSol.get(i+1))*bs.booksPerDay;
            for (int j = 0; j < bs.booksList.size(); j++) {
                if (!usedBooks.contains(bs.booksList.get(j))){
                    solAux.add(bs.booksList.get(j));
                    usedBooks.add(bs.booksList.get(j));
                }
                if (solAux.size() == readMax)break;
            }
            if (solAux.size() == 0){
                solAux.add(bs.booksList.get(0));
            }
            pw.println(bs.id+" "+solAux.size());
            for (int j = 0; j < solAux.size(); j++) {
                if (j == solAux.size()-1){
                    pw.println(solAux.get(j));
                } else {
                    pw.print(solAux.get(j)+" ");
                }
            }
        }

        pw.flush();
        pw.close();


    }

    public static class BookStore {
        public int id, bookNum, signupTime, booksPerDay;
        public ArrayList<Integer> booksList;
        public HashSet<Integer> bookSet;

        public BookStore(int id, int bookNum, int signupTime, int booksPerDay) {
            this.id = id;
            this.bookNum = bookNum;
            this.signupTime = signupTime;
            this.booksPerDay = booksPerDay;
            this.booksList = new ArrayList<>(bookNum);
            this.bookSet = new HashSet<>(bookNum);
        }

        public void addBook(int id){
            booksList.add(id);
            bookSet.add(id);
        }
    }

}
