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
 * Tests project 10 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project10.html
 *
 * This test file verifies that the Project 10 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project10Test {
  
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private static final int PROJECT_NUMBER = 10;
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
   * A helper method for testing the reset dice method.
   */
  private void runResetDice(int[] expectedResult, int[] dice) {
    Class<?>[] parameters = {int[].class};
    Object[] args = {dice};
    runStaticMethod("resetDice", parameters, args);
    assertArrayEquals(expectedResult, dice);
  }
  
  /**
   * A helper method for testing the dice to string method.
   */
  private void runDiceToString(String expectedResult, int[] dice) {
    Class<?>[] parameters = {int[].class};
    Object[] args = {dice};
    String result = (String) runStaticMethod("diceToString", parameters, args);
    assertEquals(expectedResult, result.trim());
  }
  
  /**
   * A helper method for testing the roll dice method.
   */
  private void runRollDice(int[] expectedResult, int[] dice) {
    Class<?>[] parameters = {int[].class};
    Object[] args = {dice};
    runStaticMethod("rollDice", parameters, args);
    assertArrayEquals(expectedResult, dice);
  }
  
  /**
   * A helper method for testing the prompt for reroll method.
   */
  private void runPromptForReroll(int[] expectedResult, int[] dice, String[] indices) {
    String input = buildLines(indices);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    Class<?>[] parameters = {int[].class, Scanner.class};
    Object[] args = {dice, new Scanner(System.in)};
    
    runStaticMethod("promptForReroll", parameters, args);
    assertArrayEquals(expectedResult, dice);
  }
  
  /**
   * A helper method for testing the prompt for play again method.
   */
  private void runPromptForPlayAgain(boolean expectedResult, String[] playAgain) {
    String input = buildLines(playAgain);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    
    Class<?>[] parameters = {Scanner.class};
    Object[] args = {new Scanner(System.in)};
    
    boolean result = (boolean) runStaticMethod("promptForPlayAgain", parameters, args);
    assertEquals(expectedResult, result);
  }
  
  /**
   * A helper method for testing the get result method.
   */
  private void runGetResult(String expectedResult, int[] dice) {
    runGetResult("", expectedResult, dice);
  }

  private void runGetResult(String message, String expectedResult, int[] dice) {
    Class<?>[] parameters = {int[].class};
    Object[] args = {dice};
    String result = (String) runStaticMethod("getResult", parameters, args);
    assertEquals(message, expectedResult, result);
  }
  
  /**
   * A helper method for testing the get counts method.
   */
  private void runGetCounts(int[] expectedResult, int[] dice) {
    Class<?>[] parameters = {int[].class};
    Object[] args = {dice};
    int[] result = (int[]) runStaticMethod("getCounts", parameters, args);
    assertArrayEquals(expectedResult, result);
  }
  
  @Test
  public void testResetDice() {
    runResetDice(new int[]{0, 0, 0, 0, 0}, new int[]{1, 2, 3, 4, 5});
  }
  
  @Test
  public void testDiceToString() {
    runDiceToString("0 2 7 5 4", new int[]{0, 2, 7, 5, 4});
  }
  
  @Test
  public void testRollDice() {
    runRollDice(new int[]{1,1,1,1,1}, new int[]{1,1,1,1,1});
  }
  
  @Test
  public void testPromptForReroll() {
    runPromptForReroll(new int[]{0,1,0,1,0}, new int[]{2,1,2,1,2}, new String[]{"0","2","4", "-1"});
  }
  
  @Test
  public void testPromptForPlayAgainY() {
    runPromptForPlayAgain(true, new String[]{"Y"});
  }
  
  @Test
  public void testPromptForPlayAgainy() {
    runPromptForPlayAgain(true, new String[]{"y"});
  }
  
  @Test
  public void testPromptForPlayAgainN() {
    runPromptForPlayAgain(false, new String[]{"N"});
  }
  
  @Test
  public void testPromptForPlayAgainn() {
    runPromptForPlayAgain(false, new String[]{"n"});
  }
  
  @Test
  public void testPromptForPlayAgainInvalid() {
    runPromptForPlayAgain(true, new String[]{"X", "Y"});
  }
  
  @Test
  public void testGetResultFiveOfAKind() {
    runGetResult("Five of a kind!", new int[]{4, 4, 4, 4, 4});
  }
  
  @Test
  public void testGetResultFourOfAKind() {
    runGetResult("Failed to recognize four of a kind in 4, 4, 4, 6, 4", "Four of a kind!", new int[]{4, 4, 4, 6, 4});
  }
  
  @Test
  public void testGetResultFullHouse() {
    runGetResult("Failed to recognize full house in 4, 4, 4, 6, 6", "Full House!", new int[]{4, 4, 4, 6, 6});
  }
  
  @Test
  public void testGetResultThreeOfAKind() {
    runGetResult("Failed to recognize three of a kind in 4, 4, 4, 7, 6", "Three of a kind!", new int[]{4, 4, 4, 7, 6});
  }
  
  @Test
  public void testGetResultTwoPair() {
    runGetResult("Failed to recognize two pair in 4, 4, 7, 7, 6", "Two pair!", new int[]{4, 4, 7, 7, 6});
  }
  
  @Test
  public void testGetResultOnePair() {
    runGetResult("Failed to recognize one pair in 4, 4, 7, 5, 6", "One pair!", new int[]{4, 4, 7, 5, 6});
  }
  
  @Test
  public void testGetResultHighestValue() {
    runGetResult("Failed to recognize highest value of 7 in 4, 1, 7, 5, 6", "Highest value 7", new int[]{4, 1, 7, 5, 6});
  }
  
  @Test
  public void testGetResultStraight() {
    runGetResult("Failed to get extra credit by recognizing straight in 1, 2, 3, 4, 5", "Straight!", new int[]{1, 2, 3, 4, 5});
  }
  
  @Test
  public void testGetCounts() {
    runGetCounts(new int []{3, 0, 2, 0, 0, 0, 0, 0, 0, 0}, new int []{1, 3, 1, 3, 1});
  }
}
