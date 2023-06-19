import java.io.FileNotFoundException;

import org.analyzer.FastaHandler;
import org.apache.commons.cli.*;

/**
 * Runnable Main Method for the FastaAnalyzer
 *
 * @author Daniel Tischler 6064795
 * @version JDK 1.7, 03.05.2023
 */

public class Main extends Thread {
    /**
     * Main Logic for the FastaAnalyzer. Analyses every input file.
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

        CommandLine line = parser.parse(options, args);


        FastaHandler handler = FastaHandler.getInstance();
        // Commandline Logic won't allow missing sequence types. When implementing an own logic, you can
        // use the constructor that won't need any sequence type info.
        if (line.getOptionValues("i").length == line.getOptionValues("t").length) {
            for (int i = 0; i < line.getOptionValues("i").length; i++) {
                handler.addFastaEntry(line.getOptionValues("i")[i], line.getOptionValues("t")[i]);
            }
        } else {
            System.err.println("""
                    Please provide for every infile a sequence type.
                    If it is not known, you can set the sequence type to ambiguous.
                    """);
        }

        handler.generateOutputFiles(line.getOptionValue("o"));

        System.out.println("Program finished");
    }
}