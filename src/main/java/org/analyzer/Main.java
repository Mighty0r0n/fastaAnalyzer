package org.analyzer;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("i").longOpt("Input-Path").hasArg().build());
        options.addOption(Option.builder("o").longOpt("Output-Path").hasArg().build());
        options.addOption(Option.builder("t").longOpt("Sequence-Type").hasArg().build());

        CommandLine line =  parser.parse(options, args);

        FastaHandler handler = new FastaHandler();

        handler.parseFasta(
                new File(line.getOptionValue("i")),
                line.getOptionValue("t"));
    }
}