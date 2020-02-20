package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.HashCodeInstance;
import structure.HashCodeSolution;

import java.util.*;

public class GRASPConstructive implements Constructive<structure.HashCodeInstance, structure.HashCodeSolution> {

    private double alpha;

    public GRASPConstructive(double alpha){
        this.alpha = alpha;
    }

    static class Candidate {
        private int value;

        Candidate(int node,int value){
            this.value = value;
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
    public HashCodeSolution constructSolution(HashCodeInstance instance) {

        double realAlpha;
        if(this.alpha==-1) {
            realAlpha=RandomManager.getRandom().nextDouble();
        } else realAlpha = alpha;

        HashCodeSolution solution = new HashCodeSolution(instance);
        List<Candidate> candidates = generateCandidateList(solution);
        while (!candidates.isEmpty()){
            int chosenCandidate = choseCandidate(candidates, realAlpha);
            Candidate candidate = candidates.get(chosenCandidate);

            // TODO Apply the changes to the solution, whatever we have to do

            candidates = updateCandidateList(solution, candidates, chosenCandidate);
        }

        return solution;
    }

    public List<Candidate> generateCandidateList(HashCodeSolution solution){
        List<Candidate> list = new ArrayList<>();
        //TODO: Add candidates to list
        list.sort(Comparator.comparingInt(c1 -> c1.value));
        return list;
    }

    public int choseCandidate(List<Candidate> candidateList, double realAlpha){
        // Chose and return a candidate
        double gmin = candidateList.get(candidateList.size() - 1).value;
        double gmax = candidateList.get(0).value;
        double th = gmax - realAlpha * (gmax - gmin);
        int limit = 0;

        while (limit < candidateList.size() && candidateList.get(limit).value>= th) {
            limit++;
        }

        Random rnd = new Random();

        return candidateList.get(rnd.nextInt(limit)).value;
    }

    public List<Candidate> updateCandidateList(HashCodeSolution solution, List<Candidate> candidateList, int chosenIndex){
        // can be optimized if we want, we can update the list instead of generating it from scratch
        return generateCandidateList(solution);
    }
}
