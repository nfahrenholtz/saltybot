package saltybot.app;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.common.Credentials;
import saltybot.bot.Engine;
import saltybot.bot.exception.SaltyBotException;

public class Application {

    private static final Logger LOGGER = LogManager.getFormatterLogger(Application.class);

    public static void main(final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        final Options options = createCmdOptions();

        try {
            final CommandLine commandLine = parser.parse(options, args);
            final Configuration config = createConfiguration(commandLine);

            try {
                final Credentials credentials = new Credentials(config.getUsername(), config.getPassword());
                final Engine engine = new Engine();
                engine.run(credentials);
            } catch (SaltyBotException e) {
                throw new RuntimeException(e);
            }
        } catch (final ParseException e) {
            printHelp(options);
            LOGGER.error(e);
        }
    }

    private static Configuration createConfiguration(final CommandLine commandLine) {
        return new Configuration.ConfigurationBuilder()
                .setUsername(commandLine.getOptionValue("u"))
                .setPassword(commandLine.getOptionValue("p"))
                .buildAndMakeGlobal();
    }

    private static void printHelp(final Options options) {
        final String header = "";
        final String footer = "\nFind more information at: https://github.com/nfahrenholtz/saltybot";
        new HelpFormatter().printHelp("SaltyBet", header, options, footer, true);
    }

    private static Options createCmdOptions() {
        final Options options = new Options();

        options.addOption(Option.builder("u")
                        .longOpt("user")
                        .desc("username used to log into the SaltyBet website")
                        .argName("username")
                        .hasArg()
                        .required()
                        .build()
        );

        options.addOption(Option.builder("p")
                        .longOpt("pass")
                        .desc("password used to log into the SaltyBet website")
                        .argName("password")
                        .hasArg()
                        .required()
                        .build()
        );

        options.addOption(Option.builder("d")
                        .longOpt("db-dir")
                        .desc("directory used to store the database")
                        .argName("directory")
                        .hasArg()
                        .build()
        );

        options.addOption(Option.builder("s")
                        .longOpt("db-name")
                        .desc("name of the database to use")
                        .argName("name")
                        .hasArg()
                        .build()
        );

        options.addOption(Option.builder()
                        .longOpt("help")
                        .desc("print this message")
                        .argName("help")
                        .build()
        );

        return options;
    }
}
