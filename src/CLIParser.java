import com.budhash.cliche.*;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class CLIParser {

    public Scanner data;
    public String filename;
    public String cipher;
    public String lt_cmdr_data;



    @Command // Help
    public String help() {
        return "Uses: help -- get help\n" +
                "      readtest <filepath> -- cat a file to prove that you can read files\n" +
                "      xor <filepath> <cipher> -- XOR text in file with cipher\n" +
                "      analyze <filepath> <num buckets> -- give character frequencies for text in file for each bucket\n";
    }

    @Command // Readtest
    public String readtest(String filename) {
        tricorder(filename);
        return lt_cmdr_data;
    }

    @Command // XOR
    public String xor(String filename, String cipher) {
        tricorder(filename);
        return("Accepted.");
    }

    @Command // analyze
    public String analyze(String filename, int bucketnum) {
        tricorder(filename);
        String lt_cmdr_data = data.nextLine ();
        return("Accepted.");
    }

    public static void main(String[] args) throws IOException {
        ShellFactory.createConsoleShell("", "", new CLIParser())
                .commandLoop();

    }

    public String tricorder(String filename) {
        try {
            data = new Scanner(new File(filename));
        } catch (IOException e) {
            System.out.println("Sorry but I was unable to open your file. Verify your file path and try again.");
        }
        lt_cmdr_data = data.nextLine ();
        return lt_cmdr_data;
    }
}