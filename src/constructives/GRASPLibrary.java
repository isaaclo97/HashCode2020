package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.HashCodeInstance;
import structure.HashCodeSolution;
import structure.Library;

import java.util.*;

public class GRASPLibrary implements Constructive<HashCodeInstance, HashCodeSolution> {

    private double alpha;

    public GRASPLibrary(double alpha){
        this.alpha = alpha;
    }

    @SuppressWarnings("Duplicates")
    public HashCodeSolution constructSolution(HashCodeInstance instance) {

        double realAlpha;
        if(this.alpha==-1) {
            Random rnd = new Random();
            realAlpha=rnd.nextDouble();
        } else realAlpha = alpha;

        HashCodeSolution solution = new HashCodeSolution(instance);
        List<Library> candidates = generateLibraryList(solution);
        while (!candidates.isEmpty()){
            int chosenLibrary = choseLibrary(candidates, realAlpha);
            Library candidate = candidates.get(chosenLibrary);

            solution.addLibrary(candidate);

            candidates = updateLibraryList(solution, candidates, chosenLibrary);
        }

        return solution;
    }

    public List<Library> generateLibraryList(HashCodeSolution solution){
        Set<Library> libraries = solution.getUnusedLibraries();
        List<Library> list = new ArrayList<>();
        for (Library library : libraries) {
            if(solution.canAddLibrary(library)){
                list.add(library);
            }
        }
        list.sort(Comparator.comparingLong(Library::getValue));
        return list;
    }

    public int choseLibrary(List<Library> candidateList, double realAlpha){
        // Chose and return a candidate
        double gmin = candidateList.get(candidateList.size() - 1).getValue();
        double gmax = candidateList.get(0).getValue();
        double th = gmax - realAlpha * (gmax - gmin);
        int limit = 0;

        while (limit < candidateList.size() && candidateList.get(limit).getValue()>= th) {
            limit++;
        }

        Random rnd = new Random();
        return rnd.nextInt(limit);
    }

    public List<Library> updateLibraryList(HashCodeSolution solution, List<Library> candidateList, int chosenIndex){
        // can be optimized if we want, we can update the list instead of generating it from scratch
        return generateLibraryList(solution);
    }
}
