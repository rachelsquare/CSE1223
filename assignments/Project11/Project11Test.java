import org.junit.Before;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import java.io.*;
import java.util.*;

/**
 * Tests project 11 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project11.html
 *
 * This test file verifies that the Project 11 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project11Test {
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private static final int PROJECT_NUMBER = 11;
  private static Class<?> cls = null;
  
  /**
   * Gets the class name through trial and error and assigns it
   * permanently for the duration of testing.
   */
  @BeforeClass
  public static void setUpOnce() {
    cls = getClass(getTestClasses(PROJECT_NUMBER));
  }
  
  /**
   * Sets input and output streams to local print streams for analysis.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
  }
  
  /**
   * Sets input and output streams back to normal.
   */
  @After
  public void tearDown() {
    System.setOut(System.out);
  }
  
  /**
   * Takes a set of inputs and joins them with newlines.
   *
   * @param inputs an variable length collection of strings
   * @return the input collection as a string separated by newlines
   */
  private static String buildLines(String ... inputs) {
    StringBuilder sb = new StringBuilder();
    for (String input: inputs) {
      sb.append(input);
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }
  
  /**
   * A recursive method which returns the main method from the proper class.
   *
   * @param toTest an ArrayList of strings to test
   * @return the class object
   */
  private static Class<?> getClass(ArrayList<String> toTest) {
    if (cls == null) {
      try {
        cls = Class.forName(toTest.get(0));
      } catch (ClassNotFoundException e) {
        System.err.println("Failed to find the class: " + toTest.get(0));
        toTest.remove(0);
        if (!toTest.isEmpty()) {
          cls = getClass(toTest);
        } else {
          cls = null;
          //System.exit(1);
        }
      }
    }
    return cls;
  }
  
  /**
   * A generic method for running static methods using reflection.
   */
  public static Object runStaticMethod(String methodName, Class<?>[] parameters, Object[] args) {
    Object returnValue = null;
    try {
      Method meth = cls.getDeclaredMethod(methodName, parameters);
      meth.setAccessible(true);
      String[] params = null;
      returnValue = meth.invoke(null, args);
    } catch (NoSuchMethodException e) {
      System.err.println("No method " + methodName + " for class " + cls.getName());
      //System.exit(1);
    } catch (IllegalAccessException e) {
      System.err.println("Can't invoke method " + methodName);
      //System.exit(1);
    } catch (InvocationTargetException e) {
      System.err.println("Can't target method " + methodName);
      //System.exit(1);
    }
    return returnValue;
  }
  
  /**
   * Runs the main method of the test class.
   *
   * @param toTest an array of strings to test
   */
  private void runMain(ArrayList<String> toTest) {
    Class<?>[] parameters = {String[].class};
    Object[] args = {null};
    runStaticMethod("main", parameters, args);
  }
  
  /**
   * Generates a list of test classes.
   * Add test cases to this list as you find them.
   *
   * @param project the current project number
   * @return an ArrayList of strings to test
   */
  private static ArrayList<String> getTestClasses(int project) {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project%1$s");
    toTest.add("osu.cse1223.Project%1$sa");
    toTest.add("osu.cse1223.CSEProject%1$s");
    toTest.add("cse1223.Project%1$sa");
    toTest.add("cse1223.Project%1$s");
    toTest.add("project%1$s.Project%1$s");
    toTest.add("Project%1$s");
    toTest.add("Project%1$sA");
    toTest.add("osu.cse1223.DragonsGame");
    toTest.add("Project04.DragonTrainers");
    toTest.add("Main");
    String projectNumberWhole = Integer.toString(project);
    String projectNumberPad = "0" + projectNumberWhole;
    int originalSize = toTest.size();
    for (int i = 0; i < originalSize; i++) {
      String test = toTest.get(i);
      toTest.set(i, String.format(test, projectNumberPad));
      toTest.add(String.format(test, projectNumberWhole));
      toTest.add(String.format(test, projectNumberPad).toLowerCase());
      toTest.add(String.format(test, projectNumberWhole).toLowerCase());
    }
    return toTest;
  }
  
  /**
   * Removes all newlines and spaces, so strings can be
   * compared on a content basis.
   *
   * @param input an input string
   * @return an input string stripped of all spaces and newlines
   */
  private String reduceString(String input) {
    return input.replace("\n", "").replaceAll("\\s+", "").toLowerCase();
  }
  
  /////////////////// Implementation //////////////////////////////////
  
  /**
   * Generates the expected output for testing.
   */
  private String buildSolution(String... numbers) {
    ArrayList<String> solutionList = new ArrayList<String>();
    return String.join("\n", solutionList);
  }
  
  /**
   * A helper method for testing main.
   */
  private void runMainCase(String... numbers) {
    String input = buildLines(numbers);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    // Run student solution
    runMain(getTestClasses(PROJECT_NUMBER));
    
    // Test expected output to output
    String output = outContent.toString();
    String expectedOutput = buildSolution(numbers);
    assertEquals(reduceString(expectedOutput), reduceString(output));
  }
  
  /**
   * Runs the modifyGuess method
   */
  private void runModifyGuess(String message, String expectedResult, char guess, String word, String stars) {
    Class<?>[] parameters = {char.class, String.class, String.class};
    Object[] args = {guess, word, stars};
    String result = (String) runStaticMethod("modifyGuess", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * Runs the checkChar method
   */
  private void runCheckChar(String message, int expectedResult, char guess, String word) {
    Class<?>[] parameters = {char.class, String.class};
    Object[] args = {guess, word};
    int result = (int) runStaticMethod("checkChar", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * Runs the starWord method
   */
  private void runStarWord(String message,String expectedResult, String word) {
    Class<?>[] parameters = {String.class};
    Object[] args = {word};
    String result = (String) runStaticMethod("starWord", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * Runs the checkInList method
   */
  private void runCheckInList(String message, boolean expectedResult, char guess, ArrayList<Character> chars) {
    Class<?>[] parameters = {char.class, ArrayList.class};
    Object[] args = {guess, chars};
    boolean result = (boolean) runStaticMethod("checkInList", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * Runs the getRandomWord method
   */
  private void runGetRandomWord(String message, ArrayList<String> words) {
    Class<?>[] parameters = {ArrayList.class};
    Object[] args = {words};
    String result = (String) runStaticMethod("getRandomWord", parameters, args);
    assertTrue(message, words.contains(result));
  }
  
  /**
   * Runs the checkWord method
   */
  private void runCheckWord(String message, boolean expectedResult, String guess, String solution) {
    Class<?>[] parameters = {String.class, String.class};
    Object[] args = {guess, solution};
    boolean result = (boolean) runStaticMethod("checkWord", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * Runs the getCharacterGuess method
   */
  private void runGetCharacterGuess(String message, char expectedResult, Scanner in) {
    Class<?>[] parameters = {Scanner.class};
    Object[] args = {in};
    char result = (char) runStaticMethod("getCharacterGuess", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  private void runGetList(String message, ArrayList<String> expectedResult, Scanner in) {
    Class<?>[] parameters = {Scanner.class};
    Object[] args = {in};
    ArrayList<?> result = (ArrayList<?>) runStaticMethod("getList", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  @Test
  public void testCorrectModifyGuess() {
    runModifyGuess("Failed to detect correct guess", "CAT", 'C', "CAT", "*AT");
  }
  
  @Test
  public void testIncorrectModifyGuess() {
    runModifyGuess("Failed to detect incorrect guess", "*AT", 'C', "BAT", "*AT");
  }
  
  @Test
  public void testZeroCheckChar() {
    runCheckChar("Failed to detect zero characters in string", 0, 'X', "CAT");
  }
  
  @Test
  public void testOneCheckChar() {
    runCheckChar("Failed to detect one character in string", 1, 'C', "CAT");
  }
  
  @Test
  public void testAllCheckChar() {
    runCheckChar("Failed to detect all characters in string", 3, 'Q', "QQQ");
  }
  
  @Test
  public void testStarWord() {
    runStarWord("Failed to star word", "***", "CAT");
  }
  
  @Test
  public void testTrueCheckInList() {
    ArrayList<Character> chars = new ArrayList<Character>();
    chars.add('C');
    chars.add('A');
    chars.add('T');
    runCheckInList("Failed to verify character exists", true, 'C', chars);
  }
  
  @Test
  public void testFalseCheckInList() {
    ArrayList<Character> chars = new ArrayList<Character>();
    chars.add('C');
    chars.add('A');
    chars.add('T');
    runCheckInList("Failed to verify character does not exist", false, 'X', chars);
  }
  
  @Test
  public void testGetRandomWord() {
    ArrayList<String> words = new ArrayList<String>();
    words.add("CAT");
    words.add("MIGHTY");
    runGetRandomWord("Failed to find an existing word", words);
  }
  
  @Test
  public void testTrueCheckWord() {
    runCheckWord("Failed to verify == words", true, "CAT", "CAT");
  }
  
  @Test
  public void testFalseCheckWord() {
    runCheckWord("Failed to verify != words", false, "RAT", "CAT");
  }
  
  @Test
  public void testNormalGetGuessCharacter() {
    runGetCharacterGuess("Failed to get character", 'C', new Scanner("C\n"));
  }
  
  @Test
  public void testInvalidGetGuessCharacter() {
    runGetCharacterGuess("Failed to get character", 'C', new Scanner("Rat\nC"));
  }
  
  @Test
  public void testOneWordGetlist() {
    ArrayList<String> oneWord = new ArrayList<String>();
    oneWord.add("CAT");
    runGetList("Failed to get one word from file", oneWord, new Scanner("CAT\n"));
  }
  
  @Test
  public void testManyWordsGetlist() {
    ArrayList<String> manyWords = new ArrayList<String>();
    manyWords.add("CAT");
    manyWords.add("HELLO");
    manyWords.add("MIGHTY");
    runGetList("Failed to get several words from file", manyWords, new Scanner("CAT\nHELLO\nMIGHTY\n"));
  }
}
