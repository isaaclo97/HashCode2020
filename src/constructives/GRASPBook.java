package constructives;

import structure.HashCodeSolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GRASPBook {

    private double alpha;

    public GRASPBook(double alpha) {
        this.alpha = alpha;
    }

    static class Candidate {
        private int book;
        private int value;

        Candidate(int node, int value) {
            this.value = value;
            this.book = node;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public int hashCode() {
            throw new RuntimeException("Not implemented");
        }
    }

    @SuppressWarnings("Duplicates")
    public HashCodeSolution constructSolution(HashCodeSolution solution) {

        double realAlpha;
        if (this.alpha == -1) {
            Random rnd = new Random();
            realAlpha = rnd.nextDouble();
        } else realAlpha = alpha;

        for (HashCodeSolution.LibrarySolution libs : solution.getUsadasLibrerias()) {

            int maxBooks = (solution.getInstance().getDays() - libs.getSubmitDay()-libs.getInstanceLibrary().getSignUpTime()) * libs.getInstanceLibrary().getBooksPerDay();
            if(maxBooks<0){
                //En caso de ser un número enorme poner todos los posibles
                maxBooks = libs.getUnusedBooks().size();
            }
            int counter = 0;
            List<Candidate> candidates = generateCandidateList(solution, libs);
            while (!candidates.isEmpty() && counter < maxBooks) {
                int chosenCandidate = choseCandidate(candidates, realAlpha);
                Candidate candidate = candidates.get(chosenCandidate);
                solution.sendBook(libs, candidate.book);
                candidates = updateCandidateList(solution, libs, candidates, chosenCandidate);
                counter++;
            }
        }

        return solution;
    }

    public List<Candidate> generateCandidateList(HashCodeSolution solution, HashCodeSolution.LibrarySolution lib) {
        List<Candidate> list = new ArrayList<>();

        for (int chosenBook : lib.getUnusedBooks()) {
            if(!solution.isUsedBook(chosenBook)){
                list.add(new Candidate(chosenBook, solution.getInstance().getBookScore(chosenBook)));
            }
        }

        list.sort(Comparator.comparingInt(Candidate::getValue).reversed());
        return list;
    }

    public int choseCandidate(List<Candidate> candidateList, double realAlpha) {
        // Chose and return a candidate
        double gmin = candidateList.get(candidateList.size() - 1).value;
        double gmax = candidateList.get(0).value;
        double th = gmax - realAlpha * (gmax - gmin);
        int limit = 0;

        while (limit < candidateList.size() && candidateList.get(limit).value >= th) {
            limit++;
        }

        Random rnd = new Random();

        return rnd.nextInt(limit);
    }

    public List<Candidate> updateCandidateList(HashCodeSolution solution, HashCodeSolution.LibrarySolution lib, List<Candidate> candidateList, int chosenIndex) {
        // can be optimized if we want, we can update the list instead of generating it from scratch
        return generateCandidateList(solution, lib);
    }
}
