import com.budhash.cliche.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

/*
The jarfile you need to run this code is on the Maven Repository here:

http://repo1.maven.org/maven2/com/budhash/cliche/cliche-shell/0.9.3/

under cliche-shell-0.9.3.

GitHub repo - https://github.com/garbage-code/XOR_Crypter
 */

public class CLIParser {

    public Scanner data;
    public String ltCmdrData;
    public String worked;
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
                "      xor <filepath> <cipher> -- XOR text in file with cipher - writes filepath of output to clipboard\n" +
                "      analyze <filepath> <num buckets> -- give character frequencies for text in file for each bucket\n" +
                "      Please take note that this program supports Unicode, and does everything in UTF_16BE." +
                "      Please use full filepaths. This program does not support relative paths.";
    }
    // This uses the external library Cliche in order to create the "help" for CLI. It prints some help to the screen.

    @Command // Readtest
    public void readtest(String filename) {
        tricorder(filename);
        System.out.println(ltCmdrData.substring(4));
        ltCmdrData = "";
    }
    // CLI option through Cliche. Creates "readtest", takes filename from argument, puts it through tricorder function. Returns results.
    // Also upon failing it throws a note from the developer about his library. It's slightly annoying but I can't change it reliably.

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
    /* CLI option through Cliche. Creates "xor", takes filename and cipher from arguments, processes the file with tricorder, and accepts. Pushes the file through the crypter function in order to XOR.
    The conditional is so it does not show two different results according to which argument passed and which one failed. If one argument fails to pass, the whole thing stops.    */

    @Command // analyze
    public void analyze(String filename, int bucketNum) throws IOException {
        tricorder(filename);
        List<Integer> vals = new ArrayList<Integer>();
        for (int i = 0; i < ltCmdrData.length(); i++) {
            char c = ltCmdrData.charAt(i);
            vals.add((int) c);
        }
        // Switches string to unicode integer values
        int[] key = new int[bucketNum];
        int max = 0;
        for (int i = 0; i < vals.size(); i++) {
            if (vals.get(i) > max) {
                max = vals.get(i);
            }
        }
        // Finds the maximum integer value of the characters
        int[][] possibleMessage = new int[bucketNum][max+1];
        for (int i = 0; i < vals.size(); i++) {
            int bucketNumMod = i % bucketNum;
            possibleMessage[bucketNumMod][vals.get(i)]++;
            if (possibleMessage[bucketNumMod][vals.get(i)] > possibleMessage[bucketNumMod][key[bucketNumMod]]) {
                key[bucketNumMod] = vals.get(i);
            }
        }
        // Frequency analysis, checks for most common chars
        for (int i = 0; i < bucketNum; i++) {
            key[i] = key[i] ^ 32;
        }
        // XORs everything by 32 (the space character)
        List<Character> keyChars = new ArrayList<>();
        for (int i = 0; i < key.length; i++) {
            keyChars.add((char) key[i]);
        }
        StringBuilder sb = new StringBuilder(keyChars.size());
        for (char c : keyChars)
            sb.append(c);
        // All this turns integers back into a string
        String result = sb.toString();
        System.out.println("The key, no matter how wacky, is: " + result);
        System.out.println("I am completely aware that this is not the key that you used at first. But just believe.");
    }
    /*
    CLI option through Cliche. Takes filename, and expected key length (bucketNum). Turns string into characters, than integers. Function first finds the highest value of the string integers.
    Creates a 2 dimensional array to do frequency analysis. Does frequency analysis, as well as establish the possible values. XOR's key values by 32 (space). Turns it back into readable output. Outputs.
     */

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
            System.out.println("Something went wrong.");
        }
        for (int i=0; i < lines; i++) {
            ltCmdrData += data.nextLine();
            ltCmdrData += " \n";
        }
        lines = 0;
        data = null;
    }
    // Reads files that are thrown in. Handles exceptions.


    public void crypter(String data, String cipher) {
        byte[] cipherByteArray;
        byte[] dataByteArray;
        cipherByteArray = cipher.getBytes(StandardCharsets.UTF_16BE);
        dataByteArray = data.getBytes(StandardCharsets.UTF_16BE);
        ByteArrayInputStream cipherByteStream = new ByteArrayInputStream(cipherByteArray);
        ByteArrayInputStream dataByteStream = new ByteArrayInputStream(dataByteArray);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int cipherStreamChars = cipherByteStream.available();
        byte[] intermediary = new byte[100];
        for (int i = 0; dataByteStream.available() > 0; i++) {
            outputStream.write(dataByteStream.read() ^ cipherByteStream.read(intermediary, i % cipherStreamChars, i % cipherStreamChars));
        }
        // XORs everything
        String output = "";
        String checkFor = "null";
        output = new String(outputStream.toByteArray(), StandardCharsets.UTF_16BE);
        output = output.replace(checkFor,"");
        output = output.substring(0, output.length()-2);
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
        System.out.println("Output saved to " + tempLocation + " - This filepath has been saved to your clipboard.");
        System.out.println(output);
        StringSelection selection = new StringSelection(tempLocation);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    // Encrypts/decrypts files. Takes string, turns string into byte array, then into byte array streams. XORs the bytes. Prints the output, and also saves it to .tmp file. Copies tmp filepath to clipboard (on Windows, at least.)

    public void ryanTheTemp() {
        try {
            File temp = File.createTempFile("tempfile", ".tmp");
            System.out.println("Temp File created at: " + temp.getAbsolutePath());
            tempLocation = temp.getAbsolutePath();
        } catch (IOException e) {
            ;
        }
    }
    // This function creates the temp file. It's a separate function because I originally anticipated the analyze function would also create temp files. Didn't reintegrate it.
}