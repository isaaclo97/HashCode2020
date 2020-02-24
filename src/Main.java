import algorithms.AlgConstructive;
import algorithms.AlgConstructiveLS;
import constructives.GRASPBook;
import constructives.GRASPLibrary;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.results.Experiment;
import structure.HashCodeInstance;
import structure.HashCodeInstanceFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

    private static final String[] extensions = new String[]{".txt"};

    public static void main(String[] args) throws IOException {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String date = String.format("%04d-%02d-%02d T%02d-%02d", year, month, day, hour, minute);

        String dir = ((args.length == 0) ? "instances" : (args[1] + "/"));
        String outDir = "experiments/" + date;
        File outDirCreator = new File(outDir);
        outDirCreator.mkdirs();

        ArrayList<Algorithm<HashCodeInstance>> execution = new ArrayList<>();
        execution.add(new AlgConstructiveLS(new GRASPLibrary(-1), new GRASPBook(0))); //0.25 0.75 0.5 -1 (valor de alpha aleatorio)
        //execution.add(new AlgConstructive(new GRASPLibrary(-1), new GRASPBook(-1))); //0.25 0.75 0.5 -1 (valor de alpha aleatorio)

        HashCodeInstanceFactory factory = new HashCodeInstanceFactory();
        for (int i = 0; i < execution.size(); i++) {
            String outputFile = outDir + "/" + execution.get(i).toString() + i +".xlsx";
            Experiment<structure.HashCodeInstance, structure.HashCodeInstanceFactory> experiment = new Experiment<>(execution.get(i), factory);
            experiment.launch(dir, outputFile, extensions);
        }
    }
}