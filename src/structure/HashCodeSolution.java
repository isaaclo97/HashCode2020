package structure;

import grafo.optilib.structure.Solution;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class HashCodeSolution implements Solution {

    private Double mark;
    private HashCodeInstance instance;

    public HashCodeSolution(HashCodeInstance instance) {

        this.instance = instance;
    }
    public HashCodeSolution(structure.HashCodeSolution sol) {

    }

    public Double getMark(){
        return this.mark;
    }

    public double getObjectiveFunctionValue() {
        if(this.mark==null){
            return this.mark = getMark();
        }
        return this.mark;
    }
}
