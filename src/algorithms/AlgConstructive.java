package algorithms;

import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import structure.HashCodeInstance;
import structure.HashCodeSolution;

import java.util.concurrent.TimeUnit;

public class AlgConstructive implements Algorithm<HashCodeInstance> {

	final Constructive<HashCodeInstance, HashCodeSolution> constructive;

	public AlgConstructive(Constructive<HashCodeInstance, HashCodeSolution> constructive){
		this.constructive = constructive;
	}

	@Override
	public Result execute(HashCodeInstance hashCodeInstance){
		// Compute the solution, and print the time to solution
		final long startTime = System.nanoTime();
		structure.HashCodeSolution hashCodeSolution = constructive.constructSolution(hashCodeInstance);
		long timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		Result r = new Result(hashCodeInstance.getName());

		// Evaluate the solution quality
		double seconds = timeToSolution / 1000.0;
		System.out.println("Time (s): " + seconds);

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
		return this.getClass().getSimpleName() + "(" + constructive + ")";
	}
}
