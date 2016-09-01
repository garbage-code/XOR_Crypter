import com.budhash.cliche.*;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
/*
The jarfile you need to run this code is on the Maven Repository here:

http://repo1.maven.org/maven2/com/budhash/cliche/cliche-shell/0.9.3/

under cliche-shell-0.9.3.jar
 */

public class CLIParser {

    public Scanner data;
    public String ltCmdrData;
    public String worked;
    // Var creation
    /*
    I am aware that I made worked a string instead of a boolean, but I couldn't get a boolean to work for the life of me.
    It recognized when the boolean was true, but when the boolean was false, it ceased to exist.
     */

    @Command // Help
    public String help() {
        return "Uses: help -- get help\n" +
                "      readtest <filepath> -- cat a file to prove that you can read files\n" +
                "      xor <filepath> <cipher> -- XOR text in file with cipher\n" +
                "      analyze <filepath> <num buckets> -- give character frequencies for text in file for each bucket\n";
    }
    // This uses the external library Cliche in order to create the "help" for CLI. It prints some help to the screen.

    @Command // Readtest
    public String readtest(String filename) {
        tricorder(filename);
        return ltCmdrData;
    }
    // CLI option through Cliche. Creates "readtest", takes filename from argument, puts it through tricorder function. Returns results.
    // Also upon failing it throws a note from the developer about his library. It's slightly annoying but I can't change it across computers.

    @Command // XOR
    public void xor(String filename, String cipher) {
        tricorder(filename);
        if (worked == "true") {
            System.out.println("Accepted");
        } else if (worked == "false") {
            ;
        }
    }
    /* CLI option through Cliche. Creates "xor", takes filename and cipher from arguments, processes the file with tricorder, and accepts. Nothing for cipher yet.
    The conditional is so it does not show two different results according to which argument passed and which one failed. If one argument fails to pass, the whole thing stops.    */

    @Command // analyze
    public void analyze(String filename, int bucketNum) {
        tricorder(filename);
        if (worked == "true") {
            System.out.println("Accepted");
        } else if (worked == "false") {
            ;
        }
    }
    // CLI option through Cliche. Creates "analyze", takes filename and num bucket from arguments, puts the filename through tricorder, and otherwise accepts. Conditional serves the same purpose.
    // Nothing for bucketNum yet.

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("", "", new CLIParser())
                .commandLoop();

    }
    // Manages CLI and IOException.

    public String tricorder(String filename) {
        worked = "true";
        try {
            data = new Scanner(new File(filename));
        } catch (IOException e) {
            System.out.println("Sorry but I was unable to open your file. Verify your file path and try again.");
            worked = "false";
        }
        ltCmdrData = data.nextLine ();
        return ltCmdrData;
    }
    /*
    Method: tricorder - Scans given filename and turns it into a string. If it cannot open the file, it rejects it, and continues.

    Args: filename - given by user.
     */
}