import com.budhash.cliche.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
/*
The jarfile you need to run this code is on the Maven Repository here:

http://repo1.maven.org/maven2/com/budhash/cliche/cliche-shell/0.9.3/

under cliche-shell-0.9.3.jar

The other jarfile you need to run this code is on the Apache Commons site here"

http://commons.apache.org/proper/commons-lang/download_lang.cgi

under 3.4 (Java 6.0+)
 */

public class CLIParser {

    public Scanner data;
    public String ltCmdrData;
    public String worked;
    public Character[] arrayzingGrace;
    public String tempLocation;
    public int lines;
    // Var creation
    /*
    I am aware that I made worked a string instead of a boolean, but I couldn't get a boolean to work for the life of me.
    It recognized when the boolean was true, but when the boolean was false, it ceased to exist.
     */

    @Command // Help
    public String help() {
        return "Uses: help -- get help\n" +
                "      readtest <filepath> -- cat a file to prove that you can read files\n" +
                "      xor <filepath> <cipher> -- XOR text in file with cipher - writes output to clipboard\n" +
                "      analyze <filepath> <num buckets> -- give character frequencies for text in file for each bucket\n";
    }
    // This uses the external library Cliche in order to create the "help" for CLI. It prints some help to the screen.

    @Command // Readtest
    public void readtest(String filename) {
        tricorder(filename);
        System.out.println(ltCmdrData);
        ltCmdrData = "";
    }
    // CLI option through Cliche. Creates "readtest", takes filename from argument, puts it through tricorder function. Returns results.
    // Also upon failing it throws a note from the developer about his library. It's slightly annoying but I can't change it across computers.

    @Command // XOR
    public void xor(String filename, String cipher) {
        tricorder(filename);
        if (worked == "true") {
            crypter(ltCmdrData, cipher);
        } else if (worked == "false") {
            ;
        }
        ltCmdrData = "";
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

    public void tricorder(String filename) {
        worked = "true";
        try {
            data = new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.println("Sorry but I was unable to open your file. Verify your file path and try again.");
            worked = "false";
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Something went wrong. Heck if I know.");
        }
        for (int i=0; i < lines; i++) {
            ltCmdrData += data.nextLine();
            ltCmdrData += " \n";
        }
        ltCmdrData = ltCmdrData.substring(4);
        lines = 0;
        data = null;
    }
    public void crypter(String data, String cipher) {
        char[] intermediary = cipher.toCharArray();
        Character[] arrayzingGrace = ArrayUtils.toObject(intermediary);
        StringBuilder gourd = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            gourd.append((char) (data.charAt(i) ^ arrayzingGrace[i % arrayzingGrace.length]));
        }
        String output = gourd.toString();
        ryanTheTemp();
        try {
            File file = new File(tempLocation);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(output);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            ;
        }
        System.out.println("Output saved to" + tempLocation + ". This filepath has been saved to your clipboard.");
        System.out.println(output);
        StringSelection selection = new StringSelection(tempLocation);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
    }

    public void ryanTheTemp() {
        try {
            File temp = File.createTempFile("tempfile", ".tmp");
            System.out.println("Temp File created at: " + temp.getAbsolutePath());
            tempLocation = temp.getAbsolutePath();
        } catch (IOException e) {
            ;
        }
    }
    /*
    Method: tricorder - Scans given filename and turns it into a string. If it cannot open the file, it rejects it, and continues.

    Args: filename - given by user.
     */
}