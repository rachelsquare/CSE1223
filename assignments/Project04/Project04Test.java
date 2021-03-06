import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Tests project 4 as specified by:
 * http://web.cse.ohio-state.edu/cse1223/currentsem/projects/CSE1223Project04.html
 *
 * This test file verifies that the Project 4 solution passes on the basis of
 * content rather than structure. In other words, we don't care if the output
 * doesn't structurally look exactly like the expected output. However, we do
 * care that the solution has all the expected content.
 */
public class Project04Test {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  /**
   * A Dragon Type Enum.
   */
  public enum Dragon {
    FIRE, WATER, PLANT, NONE;
  }

  /**
   * Sets input and output streams to local print streams for analysis.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  /**
   * Sets input and output streams back to normal.
   */
  @After
  public void tearDown() {
    System.setIn(System.in);
    System.setOut(System.out);
  }

  /**
   * Takes a set of inputs and joins them with newlines.
   *
   * @param inputs an variable length collection of strings
   * @return the input collection as a string separated by newlines
   */
  private String buildLines(String ... inputs) {
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
  private static Class<?> getMain(ArrayList<String> toTest) {
    Class<?> cls;
    try {
      cls = Class.forName(toTest.get(0));
    } catch (ClassNotFoundException e) {
      System.err.println("Failed to find the class: " + toTest.get(0));
      toTest.remove(0);
      if (!toTest.isEmpty()) {
        cls = getMain(toTest);
      } else {
        cls = null;
        System.exit(1);
      }
    }
    return cls;
  }

  /**
   * Runs the main method of the test class.
   *
   * @param toTest an array of strings to test
   */
  private static void runMain(ArrayList<String> toTest) {
    Class<?> cls = getMain(toTest);
    try {
      Method meth = cls.getMethod("main", String[].class);
      String[] params = null;
      meth.invoke(null, (Object) params);
    } catch (NoSuchMethodException e) {
      System.err.println("No method main");
      System.exit(1);
    } catch (IllegalAccessException e) {
      System.err.println("Can't invoke method main");
      System.exit(1);
    } catch (InvocationTargetException e) {
      System.err.println("Can't target method main");
      System.exit(1);
    }
  }

  /**
   * Generates a list of test classes.
   * Add test cases to this list as you find them.
   *
   * @param project the current project number
   * @return an ArrayList of strings to test
   */
  private ArrayList<String> getTestClasses(int project) {
    ArrayList<String> toTest = new ArrayList<String>();
    toTest.add("osu.cse1223.Project%1$s");
    toTest.add("osu.cse1223.Project%1$sa");
    toTest.add("osu.cse1223.CSEProject%1$s");
    toTest.add("cse1223.Project%1$sa");
    toTest.add("cse1223.Project%1$s");
    toTest.add("project%1$s.Project%1$s");
    toTest.add("Project%1$s");
    toTest.add("osu.cse1223.DragonsGame");
    toTest.add("Project04.DragonTrainers");
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

  /**
   * Parse the user solution for their dragon.
   */
  private String getGeneratedDragonType(String userSolution) {
    String lowercase = userSolution.toLowerCase();
    int start = lowercase.lastIndexOf("chose:");
    int end = lowercase.indexOf(" ", start + 7);
    String dragonType = lowercase.substring(start + 6, end);
    return dragonType;
  }

  /**
   * A helper method which converts a dragon string to an enum
   *
   * @param dragonType the type of the dragon as a string
   * @return the dragon type as an enum
   */
  private Dragon getDragonType(String dragonType) {
    String uppercaseDragonType = dragonType.toUpperCase();
    if (uppercaseDragonType.equals("F")) {
      dragonType = "fire";
    } else if (uppercaseDragonType.equals("P")) {
      dragonType = "plant";
    } else if (uppercaseDragonType.equals("W")) {
      dragonType = "water";
    } else if (dragonType.length() == 1) {
      dragonType = "none";
    }
    return Dragon.valueOf(dragonType.trim().toUpperCase());
  }

  /**
   * Handles the rock paper scissors and returns the result.
   *
   * @param guessDragon our guess dragon as an enum
   * @param userDragon the generated dragon as an enum
   * @return the result of the dragon game as a string
   */
  private String getComparisonResult(Dragon guessDragon, Dragon userDragon) {
    String result = "";
    String defeatsString = "%s defeats %s - %s";
    String winString = "you win!";
    String loseString = "you lose!";
    if (guessDragon == Dragon.NONE) {
      result = "You lose by default!";
    } else if (guessDragon == userDragon) {
      result = "A tie!";
    } else if (guessDragon == Dragon.FIRE && userDragon == Dragon.WATER) {
      result = String.format(defeatsString, Dragon.WATER, Dragon.FIRE, loseString);
    } else if (guessDragon == Dragon.FIRE && userDragon == Dragon.PLANT) {
      result = String.format(defeatsString, Dragon.FIRE, Dragon.PLANT, winString);
    } else if (guessDragon == Dragon.WATER && userDragon == Dragon.PLANT) {
      result = String.format(defeatsString, Dragon.PLANT, Dragon.WATER, loseString);
    } else if (guessDragon == Dragon.WATER && userDragon == Dragon.FIRE) {
      result = String.format(defeatsString, Dragon.WATER, Dragon.FIRE, winString);
    } else if (guessDragon == Dragon.PLANT && userDragon == Dragon.FIRE) {
      result = String.format(defeatsString, Dragon.FIRE, Dragon.PLANT, loseString);
    } else {
      result = String.format(defeatsString, Dragon.PLANT, Dragon.WATER, winString);
    }
    return result;
  }

  /**
   * Gets the number of solutions.
   *
   * @param output the user string
   * @return the number of solutions
   */
  private int numOfSolutions(String output) {
    int count = output.length() - output.replace("[", "").length();
    return count;
  }

  /**
   * Generates the solution for testing.
   *
   * @param guessDragon the lowercase guess dragon type string
   * @param userDragon the lowercase generated dragon type string
   * @return the expected solution string
   */
  private String buildSolution(String guessDragon, String userDragon) {
    Dragon guessDragonType = getDragonType(guessDragon);
    ArrayList<String> solutionList = new ArrayList<String>();
    solutionList.add("Please select one of your dragons [Fire/Plant/Water]:");
    if (guessDragonType == Dragon.NONE) {
      solutionList.add(String.format("You don't have a %s dragon, so you choose no dragons.", guessDragon));
    } else {
      solutionList.add(String.format("You chose: %s dragon", guessDragonType));
    }
    solutionList.add(String.format("I chose: %s dragon", userDragon));
    solutionList.add(getComparisonResult(guessDragonType, getDragonType(userDragon)));
    return String.join("\n", solutionList);
  }

  /**
   * A helper method which allows us to rapidly build test cases.
   *
   * @param dragonType the dragon under test
   */
  private void runCase(String dragonType) {
    // Create multiple records just to test the extra credit
    String input = buildLines(dragonType, dragonType, dragonType, dragonType, dragonType, dragonType, dragonType, dragonType);
    InputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);
    runMain(getTestClasses(4));
    String solutionOutput = outContent.toString();
    String[] solutionLines = solutionOutput.split("!");
    int numOfSolutions = numOfSolutions(solutionOutput);
    if (numOfSolutions > 1) {
      solutionOutput = solutionOutput.replace(solutionOutput.substring(solutionOutput.toLowerCase().lastIndexOf("out"), solutionOutput.length()), "");
    }
    int step = solutionOutput.split(System.lineSeparator()).length / numOfSolutions;
    String solution = "";
    for (int i = 0; i < numOfSolutions; i++) {
      solution += buildSolution(dragonType, getGeneratedDragonType(solutionLines[i]));
    }
    assertEquals(reduceString(solution), reduceString(solutionOutput));
  }

  /**
   * Tests the Fire dragon case.
   */
  @Test
  public void testFire() {
    runCase("Fire");
  }

  /**
   * Tests the abreviated Fire dragon case.
   */
  @Test
  public void testAbbreviatedFire() {
    runCase("F");
  }

  /**
   * Tests the lowercase Fire dragon case.
   */
  @Test
  public void testLowercaseFire() {
    runCase("fire");
  }

  /**
   * Tests the lowercase abbreviated Fire dragon case.
   */
  @Test
  public void testLowercaseAbbreviatedFire() {
    runCase("f");
  }

  /**
   * Tests the all caps Fire dragon case.
   */
  @Test
  public void testAllCapsFire() {
    runCase("FIRE");
  }

  /**
   * Tests the Water dragon case.
   */
  @Test
  public void testWater() {
    runCase("Water");
  }

  /**
   * Tests the abbreviated Water dragon case.
   */
  @Test
  public void testAbbreviatedWater() {
    runCase("W");
  }

  /**
   * Tests the lowercase Water dragon case.
   */
  @Test
  public void testLowercaseWater() {
    runCase("water");
  }

  /**
   * Tests the lowercase abbreviated Water dragon case.
   */
  @Test
  public void testLowercaseAbbreviatedWater() {
    runCase("w");
  }

  /**
   * Tests the All Caps Water dragon case.
   */
  @Test
  public void testAllCapsWater() {
    runCase("WATER");
  }

  /**
   * Tests the Plant dragon case.
   */
  @Test
  public void testPlant() {
    runCase("Plant");
  }

  /**
   * Tests the abbreviated Plant dragon case.
   */
  @Test
  public void testAbbreviatedPlant() {
    runCase("P");
  }

  /**
   * Tests the lowercase Plant dragon case.
   */
  @Test
  public void testLowercasePlant() {
    runCase("plant");
  }

  /**
   * Tests the lowercase abbreviated Plant dragon case.
   */
  @Test
  public void testLowercaseAbbreviatedPlant() {
    runCase("p");
  }

  /**
   * Tests the All Caps Plant dragon case.
   */
  @Test
  public void testAllCapsPlant() {
    runCase("PLANT");
  }

  /**
   * Tests the none dragon case.
   */
  @Test
  public void testNone() {
    runCase("q");
  }
}
