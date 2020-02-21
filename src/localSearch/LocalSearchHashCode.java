package localSearch;

import structure.HashCodeSolution;
import structure.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchHashCode {

    public HashCodeSolution improve(HashCodeSolution sol) {
//        Random rnd = new Random();
        double actVal = sol.getObjectiveFunctionValue();
        boolean improved = true;
        HashCodeSolution bestSol = sol;
        while (improved){
            improved = false;
            HashCodeSolution solCop = new HashCodeSolution(sol);
            solCop.shuffleList();
            double newPoint = solCop.getObjectiveFunctionValue();
            if(Double.compare(newPoint, actVal) > 0){
                actVal = newPoint;
                bestSol = solCop;
                improved = true;
            }

        }
        return bestSol;
    }

}
