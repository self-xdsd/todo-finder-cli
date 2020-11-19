/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.todocli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main program.
 *
 * @version $Id$
 * @checkstyle HideUtilityClassConstructor (100 lines)
 * @since 0.0.1
 */
public class TodoFinderCli {

    private static final String NAME_AND_VERSION = "TodoCLI v1.0";

    /**
     * The root directory within which to search.
     */
    private static String root = ".";

    /**
     * The main logger.
     */
    private static Logger logger;

    /**
     * The main method.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        logger = LoggerFactory.getLogger(TodoFinderCli.class);

        try {
            initOptions(args);
        } catch (ParseException e) {
            System.out.println("Error: Could not parse command line arguments.");
        }
    }

    /**
     * Initializes program options, i.e. possible command line arguments.
     *
     * @param args Command line arguments
     * @throws ParseException if an error occurs while parsing command line arguments
     */
    private static void initOptions(String[] args) throws ParseException {
        Options options = new Options();

        Option versionOption = new Option("v", "version", false,
                "print utility version to the standard output stream and exit");
        versionOption.setRequired(false);
        options.addOption(versionOption);

        CommandLineParser cmdParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(NAME_AND_VERSION, options);
            throw e;
        }

        if (cmd.hasOption("v") || cmd.hasOption("version")) {
            System.out.println(NAME_AND_VERSION);

        } else {
            run();
        }
    }

    /**
     * Runs the utility, i.e. starts walking the directory structure and
     * finding TODOs.
     */
    private static void run() {
        logger.info("Running {} from directory '" + root + "'\n", NAME_AND_VERSION);

        try {
            Files.walkFileTree(Paths.get(root), new TodoVisitor(
                    new JsonTodosSerializer(), logger
            ));
        } catch (final IOException ex) {
            System.err.println(
                    "Could not walk the given directory structure!"
            );
            ex.printStackTrace();
        }
    }
}
