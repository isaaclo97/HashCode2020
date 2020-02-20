package algorithms;

import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import structure.HashCodeInstance;
import structure.HashCodeSolution;

import java.util.concurrent.TimeUnit;

public class AlgConstructiveGrasp implements Algorithm<structure.HashCodeInstance> {

	final Constructive<structure.HashCodeInstance, HashCodeSolution> constructive;
	private Integer iterations;
	private String name;

	public AlgConstructiveGrasp(Constructive<HashCodeInstance, HashCodeSolution> constructive, Integer iterations, String name){
		this.constructive = constructive;
		this.iterations = iterations;
		this.name = name;
	}

	@SuppressWarnings("Duplicates")
	@Override
	public Result execute(structure.HashCodeInstance hashCodeInstance){
		// Compute the solution, and print the time to solution
		final long startTime = System.nanoTime();

		double sol=0;
		structure.HashCodeSolution res = null;
		for(int i=0; i<iterations;i++) {
			System.out.println("Iteration "+i+ " out of "+iterations);
			structure.HashCodeSolution hashCodeSolution = constructive.constructSolution(hashCodeInstance);
			double value = hashCodeSolution.getMark();
			if(value>sol){
				sol = value;
				res = hashCodeSolution;
			}
		}
		System.out.println();
		System.out.println("Value " + sol);

		long timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

		Result r = new Result(hashCodeInstance.getName() );

		// Evaluate the solution quality
		double seconds = timeToSolution / 1000.0;
		System.out.println("Time (s): " + seconds);

		r.add("Time (s)", seconds);
		//r.add("# Constructions", 1);
		r.add("# Global F.O Value", res.getObjectiveFunctionValue());

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
