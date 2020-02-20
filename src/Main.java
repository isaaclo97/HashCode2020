import algorithms.AlgConstructive;
import algorithms.AlgConstructiveGrasp;
import constructives.GRASPConstructive;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Experiment;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) throws IOException {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String date = String.format("%04d-%02d-%02d T%02d-%02d", year, month, day, hour, minute);

        structure.HashCodeInstanceFactory factory = new structure.HashCodeInstanceFactory();

        String dir = ((args.length == 0) ? "instances" : (args[1] + "/"));
//      String dir = ((args.length == 0) ? "instances/pruebas" : (args[1] + "/"));
        String outDir = "experiments/" + date;
        File outDirCreator = new File(outDir);
        outDirCreator.mkdirs();
        String[] extensions = new String[]{".txt"};

        ArrayList<Constructive<structure.HashCodeInstance, structure.HashCodeSolution>> constructives = new ArrayList<>();

        ArrayList<Algorithm<structure.HashCodeInstance>> execution = new ArrayList<>();

        execution.add(new AlgConstructiveGrasp(new GRASPConstructive(-1),100,"GRASP GRADO")); //0.25 0.75 0.5 -1 (valor de alpha aleatorio)
        execution.add(new AlgConstructive(new GRASPConstructive(-1))); //0.25 0.75 0.5 -1 (valor de alpha aleatorio)

        for (int i = 0; i < execution.size(); i++) {
            //String outputFile = outDir+"/"+instanceSet+"_"+execution[i].toString()+".xlsx";
            String outputFile = outDir + "/" + execution.get(i).toString() + i +".xlsx";
            Experiment<structure.HashCodeInstance, structure.HashCodeInstanceFactory> experiment = new Experiment<>(execution.get(i), factory);
            experiment.launch(dir, outputFile, extensions);
        }
    }
}