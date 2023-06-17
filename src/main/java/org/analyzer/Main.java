package org.analyzer;


import java.io.FileNotFoundException;

import org.apache.commons.cli.*;

/**
 * Runnable Main Method for the FastaAnlyzer
 *
 * @author Daniel Tischler 6064795
 * @version JDK 1.7, 03.05.2023
 */

public class Main extends Thread {
    /**
     * Main Logic for the FastaAnalyzer. class only needs to get instantiated with the File to analyse and the
     * corresponding Sequence Type
     *
     * @param args arguments given to the main from the CLI, these getting parsed by commons-cli
     * @throws FileNotFoundException incorrect filepath
     * @throws ParseException        incorrect CLI param statements
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException, WrongFastaFormatException {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("i").longOpt("Input-Path").hasArg().build());
        options.addOption(Option.builder("o").longOpt("Output-Path").hasArg().build());
        options.addOption(Option.builder("t").longOpt("Sequence-Type").hasArg().build());

        CommandLine line = parser.parse(options, args);

        FastaHandler handler = FastaHandler.getInstance(line.getOptionValue("i"), line.getOptionValue("t"));
        FastaHandler.getInstance("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/test2.fasta");
        FastaHandler.getInstance("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/test2.fasta");


        System.out.println("Programm ist fertig gelaufen. YIPPIE!");
    }
}