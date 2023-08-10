package org.analyzer;

import java.io.FileNotFoundException;

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
     * Every input file needs a corresponding sequence type
     *
     * @param args arguments given to the main from the CLI, these getting parsed by commons-cli
     * @throws FileNotFoundException incorrect filepath
     * @throws ParseException        incorrect CLI param statements
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        int defaultThreads = Runtime.getRuntime().availableProcessors() * 3 / 4;

        CommandLine line = createCommandLineParser(args, defaultThreads);

        getNumberOfThreads(line, defaultThreads);


        long startTime = System.nanoTime();

        FastaHandler handler = prepareFastaHandlerObject(line);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("-> Elapsed time: " + elapsedTimeInSeconds + " seconds for analzying Input File");

        checkForOutput(line, handler);

        // Run testRuntime() for threading benchmark
        //testRuntime();

    }

    /**
     * Simple main for testing runtime of threads.
     *
     * @param benachmark_args args are prepared in testRuntime() function
     * @throws ParseException if no parse
     */
    private static void benchmark_main(String[] benachmark_args) throws ParseException {
        int defaultThreads = Runtime.getRuntime().availableProcessors() * 3 / 4;

        CommandLine line = createCommandLineParser(benachmark_args, defaultThreads);

        getNumberOfThreads(line, defaultThreads);

        FastaHandler handler = prepareFastaHandlerObject(line);
    }

    /**
     * Experimental Runtime Test. Comment Print for clearer output. Tested over an Average of 100 runs.
     * Achieved on my Laptop:
     * 4 Thread took 0.08655225306 seconds average to run
     * 1 Thread took 0.15837162132 seconds average to run
     *
     * @throws ParseException non parsing
     */
    private static void testRuntime() throws ParseException {
        String[] args = {"-i", "TestFiles/test.fasta", "-s", "dna", "-t", "4"};
        long runtime4Threads = 0;
        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            benchmark_main(args);
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            runtime4Threads = runtime4Threads + elapsedTime;
        }


        String[] args2 = {"-i", "TestFiles/test.fasta", "-s", "dna", "-t", "1"};
        long runtime1Threads = 0;
        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            benchmark_main(args2);
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            runtime1Threads = runtime1Threads + elapsedTime;
        }
        System.out.println("4 Thread took " + ((double) runtime4Threads / 1_000_000_000.0) / 100 + " seconds average to run");
        System.out.println("1 Thread took " + ((double) runtime1Threads / 1_000_000_000.0) / 100 + " seconds average to run");

    }


    private static void getNumberOfThreads(CommandLine line, int defaultThreads) {
        int requestedThreads = Integer.parseInt(line.getOptionValue("t"));
        if (line.hasOption("t") && requestedThreads <= Runtime.getRuntime().availableProcessors()) {
            FastaHandler.getInstance().numberThreads = requestedThreads;
        } else {
            FastaHandler.getInstance().numberThreads = defaultThreads;
        }
    }

    private static void checkForOutput(CommandLine line, FastaHandler handler) {
        if (line.hasOption("o")) {
            handler.generateOutputFiles(line.getOptionValue("o"), line.hasOption("p"));
            System.out.println("\n-> Program finished generating Output-Files");
        }
    }

    private static FastaHandler prepareFastaHandlerObject(CommandLine line) {
        FastaHandler handler = FastaHandler.getInstance();

        // Commandline Logic won't allow missing sequence types. When implementing an own logic, you can
        // use the constructor that won't need any sequence type info.
        try {
            handler.generateFastaHandlerObject(line.getOptionValue("i"), line.getOptionValue("s"));
            handler.processFastaEntries();
            if (line.hasOption("v")) {
                handler.verbosePrinting();
            } else {
                System.out.println("-> Finished analyzing Entrys");
            }
        } catch (WrongSequenceTypeException | MalformattedFastaFileException wste) {
            wste.getMessage();
        }
        return handler;
    }

    private static CommandLine createCommandLineParser(String[] args, int defaultThreads) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption(Option.builder("o").argName("outpath").hasArg().longOpt("Output-Path").build());
        options.addOption(Option.builder("i").argName("infile").hasArgs().longOpt("Input-Path").valueSeparator(' ').build());
        options.addOption(Option.builder("s").argName("sequence_type").hasArgs().longOpt("Sequence-Type").valueSeparator(' ').build());
        options.addOption(Option.builder("v").argName("verbose").longOpt("Verbose").desc("Make the programm output verbose").build());
        options.addOption(Option.builder("p").argName("translate").longOpt("Peptide-Translate").desc("Translate Dna/Rna to peptide sequence").type(Integer.class).build());
        options.addOption(Option.builder("t").argName("threads").hasArg().desc("Number of threads (default: " + defaultThreads + ")").longOpt("Number of Threads").build());

        return parser.parse(options, args);
    }
}