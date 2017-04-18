package edu.gatech.seclass.replace;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.gatech.seclass.replace.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MyMainTest{
	
/*
Place all  of your tests in this class, possibly using MainTest.java as an example.
*/
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }
    // Some utilities

    private File createTmpFile() throws IOException {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private File createInputFile1() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome");

        fileWriter.close();
        return file1;
    }

    private File createInputFile2() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("LaMarcus Aldridge,");

        fileWriter.close();
        return file1;
    }

    private File createInputFile3() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("");

        fileWriter.close();
        return file1;
    }

    private File createInputFile4() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome");

        fileWriter.close();
        return file1;
    }
    // Purpose: Test when From-String is null(not existed), whether no pattern will be replaced in the file.
    // Frame #: Case1
    @Test
    public void replaceTest1() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = { "Curry", inputFile1.getPath()};
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }


    // Purpose: When the present file is not exist, whether the output will show error info.
    // Frame #: Case7
    @Test
    public void replaceTest2() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-w", "Aldridge", "Curry", inputFile1.getPath()+"error_path"};
        Main.main(args);
        assertEquals("File Not Found", errStream.toString().trim());
    }

    // Purpose: When Length of [from]String is longer than the whole file, whether no pattern will be replaced in the file.
    // Frame #: Case2
    @Test
    public void replaceTest3() throws Exception {
        File inputFile1 = createInputFile2();;

        String args[] = { "-f", "The Spurs trail the Warriors by a half-game in the West", "Aldridge", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: When file size is empty, whether no pattern will be replaced in the file.
    // Frame #: Case5
    @Test
    public void replaceTest4() throws Exception {
        File inputFile1 = createInputFile3();;

        String args[] = { "-f", "Aldridge", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: When Number of occurrences of the [from]String in the file is zero, whether no pattern will be replaced in the file.
    // Frame #: Case6
    @Test
    public void replaceTest5() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "CCCCCCCCCCCCCCCCCC", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: When OPT(-i or -w or -f) is exist and Dash (-) is not used in OPT, whether the output show error info
    // Frame #: Case8
    // Type c: The file should not be replaced when the Dash(-) is forgotten to be used in OPT; should show the usage() information. The replace method still can identify "-f", even f is not corrected format
    @Test
    public void replaceTest6() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "f", "Aldridge", "Curry", inputFile1.getPath()};
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose:  When [from]String is multi-characters; both [from] and [to] String is no-whitepace,no-quotes presence and no enclosing quotes; file size is not empty;
    // Number of occurrences of the [from]String in the file is one and at the beginning; no OPT opinion.

    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  None
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  <n/a>

    // Frame #: Case39
    @Test
    public void replaceTest7() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "LaMarcus", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Curry Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //    Length of [from]String                                :  Many
    //    Length of [to]String                                  :  Many
    //    Whitespace in [from] and [to]String                   :  No
    //    Presence of quotes in [from] and [to]String           :  No
    //    Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //    File Size                                             :  Not empty
    //    Number of occurrences of the [from]String in the file :  One
    //    Position of the [from]String in the file              :  Beginning
    //    Presence of a file corresponding to the name          :  Present
    //    Number of OPT opinion                                 :  One
    //    OPT category                                          :  -f
    //    OPT argument order                                    :  <n/a>
    //    Dash (-) used in OPT                                  :  Yes

    // Frame #: Case40
    @Test
    public void replaceTest8() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "LaMarcus", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Curry Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  in order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case43
    @Test
    public void replaceTest9() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "-i",  "-w", "laMarcus", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Curry Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  random order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case44
    @Test
    public void replaceTest10() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-i", "-w", "-f", "laMarcus", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Curry Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  None
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  <n/a>

    // Frame #: Case45
    @Test
    public void replaceTest11() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "syndrome", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -f
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case46
    @Test
    public void replaceTest12() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "syndrome", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -i
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case47
    @Test
    public void replaceTest13() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-i", "SYNdrome", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -w
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case48
    @Test
    public void replaceTest14() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-w", "syndrome", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  No
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Not enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  in order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case49
    @Test
    public void replaceTest15() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "-w", "syndrome", "Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Additional 15 test cases.
    // Purpose: Test when To-String is null(not existed), whether no pattern will be replaced in the file.
    // Frame #: Case3
    @Test
    public void replaceTest16() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = { "LaMarcus", inputFile1.getPath()};
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  None
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  <n/a>

    // Frame #: Case9
    @Test
    public void replaceTest17() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "LaMarcus Aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -f
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case10
    @Test
    public void replaceTest18() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-f", "LaMarcus Aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -i
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case11
    @Test
    public void replaceTest19() throws Exception {
        File inputFile1 = createInputFile1();;

        String args[] = { "-i", "lamarcus aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -w
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case12
    @Test
    public void replaceTest20() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-w", "LaMarcus Aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  in order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case13
    @Test
    public void replaceTest21() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-f", "-i", "-w", "lamarcus aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Beginning
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  random order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case14
    @Test
    public void replaceTest22() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-w", "-i", "-f", "lamarcus aldridge", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Stephen Curry ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  None
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  <n/a>

    // Frame #: Case15
    @Test
    public void replaceTest23() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "Wolff-Parkinson-White syndrome", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Stephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -f
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case16
    @Test
    public void replaceTest24() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-f", "olff-Parkinson-White syndrome", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with WStephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -i
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case17
    @Test
    public void replaceTest25() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-i", "olff-parkinson-white syndrome", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with WStephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -w
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case18
    @Test
    public void replaceTest26() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-w", "Wolff-Parkinson-White syndrome", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Stephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  in order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case19
    @Test
    public void replaceTest27() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-f", "-w", "Wolff-Parkinson-White syndrome", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Stephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  End
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  Many
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  random order of (-f,-i,-w)
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case20
    @Test
    public void replaceTest28() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-i", "-w","-f", "wOLFF-Parkinson-White syndroME", "Stephen Curry", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He went through a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Stephen Curry";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Any
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  None
    //   OPT category                                          :  <n/a>
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  <n/a>

    // Frame #: Case21
    @Test
    public void replaceTest29() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "went through", "GO THROUGH", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He GO THROUGH a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose:
    //   Length of [from]String                                :  Many
    //   Length of [to]String                                  :  Many
    //   Whitespace in [from] and [to]String                   :  Yes
    //   Presence of quotes in [from] and [to]String           :  No
    //   Presence of enclosing quotes in [from] and [to]String :  Enclosed
    //   File Size                                             :  Not empty
    //   Number of occurrences of the [from]String in the file :  One
    //   Position of the [from]String in the file              :  Any
    //   Presence of a file corresponding to the name          :  Present
    //   Number of OPT opinion                                 :  One
    //   OPT category                                          :  -f
    //   OPT argument order                                    :  <n/a>
    //   Dash (-) used in OPT                                  :  Yes

    // Frame #: Case22
    @Test
    public void replaceTest30() throws Exception {
        File inputFile1 = createInputFile4();;

        String args[] = { "-f", "went through", "GO THROUGH", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "LaMarcus Aldridge ,\n" +
                "Aldridge was cleared to return to full basketball activities\n" +
                "He GO THROUGH a bevy of tests\n" +
                "The Spurs trail the Warriors by a half-game in the West.\n" +
                "Aldridge was diagnosed with Wolff-Parkinson-White syndrome";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Instructor Provided
    private File createInputFile1_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Billy,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy\" to Bill again!");

        fileWriter.close();
        return file1;
    }

    private File createInputFile2_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Bill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than William.\n" +
                "Bill is shorter,\n" +
                "and Bill is\n" +
                "first alphabetically.");

        fileWriter.close();
        return file1;
    }

    private File createInputFile3_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill, have you learned your abc and 123?\n" +
                "I know My Abc's.\n" +
                "It is important to know your abc's and 123's,\n" +
                "so repeat with me: abc! 123! Abc and 123!");

        fileWriter.close();
        return file1;
    }

    private File createInputFile4_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("x-w|y-x|i-f|z-k\r" +
                "i-f|x-w|y-x|z-k\r" +
                "z-r|x-w|z-k|i-f");

        fileWriter.close();
        return file1;
    }

    private File createInputFile5_instr() throws Exception {
        File file = createTmpFile();
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write("");

        fileWriter.close();
        return file;
    }

    private File createInputFile6_instr() throws Exception {
        File file = createTmpFile();
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write("The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f");

        fileWriter.close();
        return file;
    }

    private File createInputFile7_instr() throws Exception {
        File file = createTmpFile();
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write("-- -- -- --");

        fileWriter.close();
        return file;
    }

    private File createInputFile8_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Mary had a csv,csv,csv\n" +
                "Mary had a csv,commas, in a csv.\n" +
                "csv,csv,csv");

        fileWriter.close();
        return file1;
    }

    private File createInputFile9_instr() throws Exception {
        File file1 =  createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("There was a man with a bad reflex\n" +
                "He made up tests that were too complex\n" +
                "googolplexmetroplexmultiplex");

        fileWriter.close();
        return file1;
    }

    // test cases

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 1 from assignment directions
    @Test
    public void mainTest1() throws Exception {
        File inputFile1 = createInputFile1_instr();

        String args[] = {"-i", "Howdy", "Hello", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Hello Billy,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"Hello\" to Bill again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 2 from assignment directions
    @Test
    public void mainTest2() throws Exception {
        File inputFile1 = createInputFile1_instr();

        String args[] = {"-w", "-f", "Bill", "William", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Billy,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy\" to William again!";

        String actual1 = getFileContent(inputFile1.getPath());

        assertEquals("The files differ!", expected1, actual1);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 3 from assignment directions
    @Test
    public void mainTest3() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"-w", "Bill is", "William is", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Bill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than William.\n" +
                "William is shorter,\n" +
                "and William is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 4 from assignment directions
    @Test
    public void mainTest4() throws Exception {
        File inputFile3 = createInputFile3_instr();

        String args[] = {"-w", "-i", "abc", "ABC", inputFile3.getPath()};
        Main.main(args);

        String expected3 = "Howdy Bill, have you learned your ABC and 123?\n" +
                "I know My Abc's.\n" +
                "It is important to know your abc's and 123's,\n" +
                "so repeat with me: abc! 123! ABC and 123!";

        String actual3 = getFileContent(inputFile3.getPath());

        assertEquals("The files differ!", expected3, actual3);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest5() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"-x", "-i", "xill", "Jill", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Jill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than Jilliam.\n" +
                "Jill is shorter,\n" +
                "and Jill is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
    }


    // Purpose: To provide an example of a test case format
    // Frame #: Instructor error example
    @Test
    public void mainTest6() throws Exception {
        String args[] = null; //invalid argument
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest7() throws Exception {
        File inputFile9 = createInputFile9_instr();

        String args[] = {"-x", "q", "qlex", "Alex", inputFile9.getPath()};
        String expected9 = "There was a man with a bad reAlex\n" +
                "He made up tests that were too comAlex\n" +
                "googolAlexmetroAlexmultiAlex";

        Main.main(args);

        String actual9 = getFileContent(inputFile9.getPath());

        assertEquals("The files differ!", expected9, actual9);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest8() throws Exception {
        File inputFile8 = createInputFile8_instr();

        String args[] = {"-w", ",", "csv", "xml", inputFile8.getPath()};
        Main.main(args);

        String expected8 = "Mary had a csv,xml,csv\n" +
                "Mary had a csv,commas, in a csv.\n" +
                "csv,xml,xml";

        String actual8 = getFileContent(inputFile8.getPath());

        assertEquals("The files differ!", expected8, actual8);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest9() throws Exception {
        File inputFile7 = createInputFile7_instr();

        String args[] = {"-i", "--", "-i", inputFile7.getPath()};
        Main.main(args);

        String expected7 = "-i -i -i -i";

        String actual7 = getFileContent(inputFile7.getPath());

        assertEquals("The files differ!", expected7, actual7);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest10() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "\"", "-x", "x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-x\" with" + System.lineSeparator() +
                "string \"-x\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest11() throws Exception {
        File inputFile4 = createInputFile4_instr();

        String args[] = {"-i", "-x", "-w", inputFile4.getPath()};
        Main.main(args);

        String expected4 = "x-w|y-w|i-f|z-k\r" +
                "i-f|x-w|y-w|z-k\r" +
                "z-r|x-w|z-k|i-f";

        String actual4 = getFileContent(inputFile4.getPath());

        assertEquals("The files differ!", expected4, actual4);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest12() throws Exception {
        File inputFile5 = createInputFile5_instr();

        String args[] = {"Bill", "Jill", inputFile5.getPath()};
        Main.main(args);

        String expected5 = "";

        String actual5 = getFileContent(inputFile5.getPath());

        assertEquals("The files differ!", expected5, actual5);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest13() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {" ", "_", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Bill_is,\n" +
                "in_my_opinion,\n" +
                "an_easier_name_to_spell_than_William.\n" +
                "Bill_is_shorter,\n" +
                "and_Bill_is\n" +
                "first_alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest14() throws Exception {
        File inputFile4 = createInputFile4_instr();

        String args[] = {"-f", "-w", "|", "x-w", "w-x", inputFile4.getPath()};
        Main.main(args);

        String expected4 = "w-x|y-x|i-f|z-k\r" +
                "i-f|x-w|y-x|z-k\r" +
                "z-r|x-w|z-k|i-f";

        String actual4 = getFileContent(inputFile4.getPath());

        assertEquals("The files differ!", expected4, actual4);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest15() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-i", "-i", "-i", "-i", "-k", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-k\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -k and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest16() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"-y", "qILL", "Jill", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Bill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than William.\n" +
                "Bill is shorter,\n" +
                "and Bill is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest17() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"Bill", "Ted", "--", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Bill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than William.\n" +
                "Bill is shorter,\n" +
                "and Bill is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest18() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"Bill", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Bill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than William.\n" +
                "Bill is shorter,\n" +
                "and Bill is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest19() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-f", "-i", "-f", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-f\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest20() throws Exception {
        File inputFile7 = createInputFile7_instr();

        String args[] = {"-w", "--", "!!", inputFile7.getPath()};
        Main.main(args);

        String expected7 = "!! !! !! !!";

        String actual7 = getFileContent(inputFile7.getPath());

        assertEquals("The files differ!", expected7, actual7);

        String args2[] = {"-w", "!!", "%%", inputFile7.getPath()};
        Main.main(args2);

        expected7 = "%% %% %% %%";

        actual7 = getFileContent(inputFile7.getPath());

        assertEquals("The files differ!", expected7, actual7);
    }



    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest21() throws Exception {
        File inputFile7 = createInputFile7_instr();

        String args[] = {" --", "##", inputFile7.getPath()};
        Main.main(args);

        String expected7 = "--######";

        String actual7 = getFileContent(inputFile7.getPath());

        assertEquals("The files differ!", expected7, actual7);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest22() throws Exception {
        File inputFile4 = createInputFile4_instr();

        String args[] = {"-w", "|", "x-w", "w-x", inputFile4.getPath()};
        Main.main(args);

        String expected4 = "w-x|y-x|i-f|z-k\r" +
                "i-f|w-x|y-x|z-k\r" +
                "z-r|w-x|z-k|i-f";

        String actual4 = getFileContent(inputFile4.getPath());

        assertEquals("The files differ!", expected4, actual4);
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest23() throws Exception {
        File inputFile2 = createInputFile2_instr();

        String args[] = {"-x", "q", "-i", "qILL", "Jill", inputFile2.getPath()};
        Main.main(args);

        String expected2 = "Jill is,\n" +
                "in my opinion,\n" +
                "an easier name to spell than Jilliam.\n" +
                "Jill is shorter,\n" +
                "and Jill is\n" +
                "first alphabetically.";

        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals("The files differ!", expected2, actual2);

    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest24() throws Exception {

        String args[] = {"-x", "q", "-i", "qILL", "Jill", "doesnotexist.txt"};
        Main.main(args);

        assertEquals("File Not Found", errStream.toString().trim());
    }

    // Purpose: To provide an example of a test case format
    // Frame #: Instructor example 5 from assignment directions
    @Test
    public void mainTest25() throws Exception {
        File inputFile9 = createInputFile9_instr();

        String args[] = {"-x", "l", "llex", "Alex", inputFile9.getPath()};
        String expected9 = "There was a man with a bad reAlex\n" +
                "He made up tests that were too comAlex\n" +
                "googolAlexmetroAlexmultiAlex";

        Main.main(args);

        String actual9 = getFileContent(inputFile9.getPath());

        assertEquals("The files differ!", expected9, actual9);
    }

    @Test
    public void replaceTest31() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "\"", "-i", "-x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-x\" with" + System.lineSeparator() +
                "string \"-x\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest32() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "\"","-f", "-i", "-x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-x\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest33() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "-f", "-i", "-x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -x and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest34() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "-f", "-i", "-x", "x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);

        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -x and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Type d: The format for -x OPT was wrong, should only followed by char(lenght==1), should show error info.
    @Test
    public void replaceTest35() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "-f", "-i", "-x", "ab", "-x", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -x and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest36() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "ab", "-f", "-i", "-x", "abc", "-x", "-x", inputFile6.getPath()};
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());

    }

    // Type d: This is uncatched exception for Index Out of Bound.If the wildcard char is not in the from string, it causes the uncatched exception
    @Test
    public void replaceTest37() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "\"", "-f", "-i", "-x", "i",  "-x", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-x\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Type d: For same OPT, the replace method is support to implement multiple declaration for the same OPT. But it seems that can only
    // identify the first four OPT; For my testcase38, it cannont find the "-x" OPT.
    @Test
    public void replaceTest38() throws Exception {
        File inputFile6 = createInputFile6_instr();

        String args[] = {"-w", "\"", "-w", "-f", "-i", "-x", "-x", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -x and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    private File createInputFile5() throws Exception {
        File file = createTmpFile();
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write("The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f");

        fileWriter.close();
        return file;
    }

    @Test
    public void replaceTest39() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w","-x", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -x and -x";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest40() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w","z","-x", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest41() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w","z",null, "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest42() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w","z","", "-x", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    @Test
    public void replaceTest43() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w", "z","-x", null, inputFile6.getPath()};
        Main.main(args);
        String expected6 = "The goal here is to replace string \"-i\" with" + System.lineSeparator() +
                "string \"-f\". Since we may also want to do multiple replacements," + System.lineSeparator() +
                "we will repeat the two strings here: -i and -f";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }


    @Test
    public void replaceTest44() throws Exception {
        File inputFile6 = createInputFile5();

        String args[] = {"-x", "-w", "z","-x", "-x", null};
        Main.main(args);
        assertEquals("Usage: Replace [-f] [-i] [-w[char]] [-x[char]] <from> <to> <filename>", errStream.toString().trim());
    }

    private File createInputFile6() throws Exception {
        File file = createTmpFile();
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write("var1 val1 vam1\n"+
                "var1\" \"val1\" \"vam1\r" +
                "\"vao1\" \"vap1");

        fileWriter.close();
        return file;
    }

    // Type d: In line 2 the "var1" should not be replaced, since the delimiter is whitespace, not "\""
    @Test
    public void replaceTest45() throws Exception {
        File inputFile6 = createInputFile6();

        String args[] = {"-x", "r", "-w","var1", "var2", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "var2 var2 var2\n" +
                "var1\" \"val1\" \"vam1\r" +
                "\"vao1\" \"vap1";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

    // Type d: In the end of file, "vap1" should also be replaced by "var2". Since wildcard is "r" and "vap1" right side is end of file(\Z) should also be the delimiter.
    @Test
    public void replaceTest46() throws Exception {
        File inputFile6 = createInputFile6();

        String args[] = {"-x", "r", "-w","\"", "var1", "var2", inputFile6.getPath()};
        Main.main(args);
        String expected6 = "var1 val1 vam1\n" +
                "var1\" \"var2\" \"vam1\r" +
                "\"var2\" \"var2";

        String actual6 = getFileContent(inputFile6.getPath());

        assertEquals("The files differ!", expected6, actual6);
    }

}
