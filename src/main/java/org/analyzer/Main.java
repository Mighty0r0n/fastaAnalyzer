package org.analyzer;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.cli.*;

/**
 * Main logic for analyzing the fasta file, its only needes to call the FastaHandler methods here.
 * All the calculations are been done automatically in the background.
 *
 * @author Daniel Tischler 6064795
 * @version JDK 1.7, 03.05.2023
 */

public class Main {
    /**
     * @param args arguments given to the main from the CLI, these getting parsed by commons-cli
     * @throws FileNotFoundException incorrect filepath
     * @throws ParseException        incorrect CLI param statements
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("i").longOpt("Input-Path").hasArg().build());
        options.addOption(Option.builder("o").longOpt("Output-Path").hasArg().build());
        options.addOption(Option.builder("t").longOpt("Sequence-Type").hasArg().build());

        CommandLine line = parser.parse(options, args);

        SequenceType seqType = SequenceType.valueOf(line.getOptionValue("t").toUpperCase());


        FastaHandler handler = FastaHandler.getInstance(seqType);

        handler.parseFasta(
                new File(line.getOptionValue("i")));

        System.out.println("Programm ist fertig gelaufen. YIPPIE!");
    }
}