package localSearch;

import structure.HashCodeSolution;
import structure.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchHashCode {

    public HashCodeSolution improve(HashCodeSolution sol) {
//      Random rnd = new Random();
        double actVal = sol.getObjectiveFunctionValue();
        boolean improved = true;
        HashCodeSolution bestSol = new HashCodeSolution(sol);
        while (improved){
            improved = false;
            for(int i=0; i<100;i++) {
                HashCodeSolution solCop = new HashCodeSolution(sol);
                solCop.shuffleList();
                double newPoint = solCop.getObjectiveFunctionValue();
                if (Double.compare(newPoint, actVal) > 0) {
                    actVal = newPoint;
                    bestSol = new HashCodeSolution(solCop);
                    improved = true;
                    //System.out.println("Mejorada en BL - " + actVal);
                }
            }
        }
        return bestSol;
    }

}
