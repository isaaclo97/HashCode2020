package algorithms;

import constructives.GRASPBook;
import constructives.GRASPLibrary;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import structure.HashCodeInstance;
import structure.HashCodeSolution;

import java.util.concurrent.TimeUnit;

public class AlgConstructive implements Algorithm<HashCodeInstance> {

	final GRASPLibrary graspLibrary;
	final GRASPBook graspBook;

	public AlgConstructive(GRASPLibrary graspLibrary, GRASPBook graspBook){
		this.graspLibrary = graspLibrary;
		this.graspBook = graspBook;
	}

	@Override
	public Result execute(HashCodeInstance hashCodeInstance){
		// Compute the solution, and print the time to solution
		final long startTime = System.nanoTime();
		HashCodeSolution hashCodeSolution = graspLibrary.constructSolution(hashCodeInstance);
		hashCodeSolution = graspBook.constructSolution(hashCodeSolution);
		long timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		Result r = new Result(hashCodeInstance.getName());

		// Evaluate the solution quality
		double seconds = timeToSolution / 1000.0;
		System.out.println("Time (s): " + seconds);
		System.out.println("Value: " + hashCodeSolution.getObjectiveFunctionValue());

		r.add("Time (s)", seconds);
		//r.add("# Constructions", 1);

		// Left this out because hashCode is not implemented in RP solution
		// r.add("# Different Solutions", allSols.size());

		r.add("# Global F.O Value", hashCodeSolution.getObjectiveFunctionValue());

		return r;
	}

	@Override
	public Solution getBestSolution() {
		return null;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "(huehue)";
	}
}
