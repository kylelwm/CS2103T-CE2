import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TextBuddyTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	//Before every test
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	//After every test
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testSuccessfulAdding() throws Exception {
		
		//Setting up my textBuddy
		TextBuddy.textList = new ArrayList<String>();
		TextBuddy.fileName = "fileName.txt";
		
		//User command => add, user input => test
		String[] commandOne = {"add", "test"};
		TextBuddy.executeInput(commandOne);
		assertEquals("added to fileName.txt: \"test\"\n", outContent.toString());
	}
	
	@Test
	public void testSuccessfulDeleting() throws Exception {
		
		//Setting up my textBuddy
		//Start with a list with 1 entry called "test"
		TextBuddy.textList = new ArrayList<String>();
		TextBuddy.textList.add("test");
		TextBuddy.fileName = "fileName.txt";
		
		//User command => delete, user input => 1
		String[] commandTwo = {"delete", "1"};
		TextBuddy.executeInput(commandTwo);
		assertEquals("deleted from fileName.txt: \"test\"\n", outContent.toString());	
	}
	
	@Test
	public void testSuccessfulDisplay() throws Exception {
		//Setting up my textBuddy
		//Start with a list with 3 entries called "test1", "test2", "test3" respectively
		TextBuddy.textList = new ArrayList<String>();
		TextBuddy.textList.add("test1");
		TextBuddy.textList.add("test2");
		TextBuddy.textList.add("test3");
		TextBuddy.fileName = "fileName.txt";
		
		//User command => display
		String[] commandThree = {"display"};
		TextBuddy.executeInput(commandThree);
		assertEquals("1.test1\n2.test2\n3.test3\n", outContent.toString());
	}
	
	@Test
	public void testSuccessfulClear() throws Exception {
		//Setting up my textBuddy
		//Start with a list with 3 entries called "test1", "test2", "test3" respectively
		TextBuddy.textList = new ArrayList<String>();
		TextBuddy.textList.add("test1");
		TextBuddy.textList.add("test2");
		TextBuddy.textList.add("test3");
		TextBuddy.fileName = "fileName.txt";
		
		//User command => clear
		String[] commandFour = {"clear"};
		TextBuddy.executeInput(commandFour);
		assertEquals("all content deleted from fileName.txt\n", outContent.toString());
	}
	
	@Test
	public void testSuccessfulExit() throws Exception {
		//Setting up my textBuddy
		//Start with a list with 1 entry called "test"
		TextBuddy.textList = new ArrayList<String>();
		TextBuddy.textList.add("test");
		TextBuddy.fileName = "fileName.txt";
		TextBuddy.textFile = new File(TextBuddy.fileName);
		
		//User command => clear
		String[] commandFive = {"exit"};
		TextBuddy.executeInput(commandFive);
		assertEquals("fileName.txt saved\nThank you for using textbuddy\n", outContent.toString());
	}
}
