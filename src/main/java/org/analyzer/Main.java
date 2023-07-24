package org.analyzer;

import java.io.FileNotFoundException;

import org.analyzer.FastaHandler;
import org.analyzer.MalformattedFastaFileException;
import org.analyzer.WrongSequenceTypeException;
import org.apache.commons.cli.*;

/**
 * Runnable org.analyzer.Main Method for the FastaAnalyzer
 *
 * @author Daniel Tischler 6064795
 * @version JDK 1.7, 03.05.2023
 */

public class Main {
    /**
     * org.analyzer.Main Logic for the FastaAnalyzer. Analyses every input file.
     * Every input file needs a correpsonding sequence type
     *
     * @param args arguments given to the main from the CLI, these getting parsed by commons-cli
     * @throws FileNotFoundException incorrect filepath
     * @throws ParseException        incorrect CLI param statements
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("o").argName("outpath").hasArg().longOpt("Output-Path").build());
        options.addOption(Option.builder("i").argName("infile").hasArgs().longOpt("Input-Path").valueSeparator(' ').build());
        options.addOption(Option.builder("t").argName("type").hasArgs().longOpt("Sequence-Type").valueSeparator(' ').build());
        options.addOption(Option.builder("v").argName("verbose").longOpt("Verbose").desc("Make the programm output verbose").build());
        options.addOption(Option.builder("p").argName("translate").longOpt("Peptide-Translate").desc("Translate Dna/Rna to peptide sequence").build());

        CommandLine line = parser.parse(options, args);


        FastaHandler handler = FastaHandler.getInstance();
        // Commandline Logic won't allow missing sequence types. When implementing an own logic, you can
        // use the constructor that won't need any sequence type info.
        if (line.getOptionValues("i").length == line.getOptionValues("t").length) {
            for (int i = 0; i < line.getOptionValues("i").length; i++) {
                try {
                    handler.addFastaEntry(line.getOptionValues("i")[i], line.getOptionValues("t")[i]);
                } catch (WrongSequenceTypeException | MalformattedFastaFileException wste) {
                    wste.printStackTrace();
                }
            }
        } else {
            System.err.println("""
                    Please provide for every infile a sequence type.
                    If it is not known, you can set the sequence type to ambiguous.
                    """);
        }
        handler.generateOutputFiles(line.getOptionValue("o"), line.hasOption("v"), line.hasOption("p"));
        System.out.println("\nProgram finished");
    }
}