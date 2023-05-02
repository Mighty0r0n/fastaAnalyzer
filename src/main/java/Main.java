import org.analyzer.FastaHandler;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;


// For testing data accessmodificators
public class Main {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        File fasta = new File("/home/daniel/IdeaProjects/fastaAnalyzer/src/main/java/org/analyzer/test.fasta");

        FastaHandler handler = new FastaHandler();
        handler.parseFasta(
                fasta,
                "peptide");


        System.out.println("hallo");

    }
}
