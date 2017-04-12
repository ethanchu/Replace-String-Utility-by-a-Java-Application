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
}
