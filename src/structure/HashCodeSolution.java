package structure;

import grafo.optilib.structure.Solution;

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

    public HashCodeInstance getInstance() {
        return instance;
    }
}
