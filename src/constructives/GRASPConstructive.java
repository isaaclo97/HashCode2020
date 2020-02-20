package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.HashCodeSolution;

import java.util.*;

public class GRASPConstructive implements Constructive<structure.HashCodeInstance, structure.HashCodeSolution> {

    private Double alpha;
    public GRASPConstructive(double alpha){
        this.alpha = alpha;
    }

    static class Candidate {

        private int node;
        private int value;

        Candidate(int node,int value){
            this.node = node;
            this.value = value;
        }
        public int getNode() {
            return node;
        }

        public void setNode(int node) {
            this.node = node;
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
    public HashCodeSolution constructSolution(structure.HashCodeInstance instance) {
        double realAlpha;
        if(this.alpha==-1) {
            realAlpha=RandomManager.getRandom().nextDouble();
        } else realAlpha = alpha;

        //int vertexIndex = rand.nextInt(graph.size() - 0 + 1) + 0;
        //int vertex = nodes.get(vertexIndex);
        ArrayList<Integer> S = new ArrayList<>();
        Map<Integer, Candidate> CL = new HashMap<>();
        //S.add(vertex);
        int node = 0;
        int sum = 0;
        int solution = 5;
        CL.put(node, new Candidate(node, sum));
        for(int i=0; i<solution ;i++){
            double gmin = Integer.MAX_VALUE;
            double gmax = 0;
            for (Candidate aCL : CL.values()) {
                int value = aCL.getValue();
                gmin = Math.min(gmin, value);
                gmax = Math.max(gmax, value);
            }
            //Ordenar porque el minimo y el maximo esta al final y con BB tengo el valor random
            //double umin = gmin+alpha*(gmax-gmin);
            double umax = gmax-realAlpha*(gmax-gmin);
            ArrayList<Candidate> RCL = new ArrayList<>();
            for (Candidate aCL : CL.values()) {
                if (aCL.getValue() >= umax) RCL.add(aCL);
            }
            assert RCL.size()==0:"Error RCL 0";
            int randomIndex = RandomManager.getRandom().nextInt(RCL.size());
            assert RCL.size()>randomIndex:"Error generating Random";
            int selectedNode = RCL.get(randomIndex).getNode();
            S.add(selectedNode);
            CL.remove(selectedNode);
        }
        HashCodeSolution s = new HashCodeSolution(instance);
        return s;
    }
}
