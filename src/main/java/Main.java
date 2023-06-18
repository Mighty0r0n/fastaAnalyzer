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
     * Main Logic for the FastaAnalyzer. class only needs to get instantiated with the File to analyse and the
     * corresponding Sequence Type
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

        for ( int i = 0; i < line.getOptionValues("i").length ; i++) {
            FastaHandler handler = FastaHandler.getInstance(line.getOptionValues("i")[i], line.getOptionValues("t")[i]);
            handler.generateOutputFiles(line.getOptionValue("o"));
        }

        System.out.println("Program finished");
    }
}