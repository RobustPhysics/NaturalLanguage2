import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Lexicon
{
	private static Lexicon instance = null;
	HashMap<String, LexiconWord> wordList;
	
	private Lexicon()
	{
		wordList = new HashMap<String, LexiconWord>();
		initializeDefault();
		printWordList();
	}
	
	public static Lexicon getInstance()
	{
		if (instance == null)
			instance = new Lexicon();
		
		return instance;
	}
	
	private void printWordList()
	{
		Iterator<Entry<String, LexiconWord>> words = wordList.entrySet().iterator();
		
		while (words.hasNext())
		{
			Map.Entry<String, LexiconWord> element = (Map.Entry<String, LexiconWord>) words.next();
			System.out.println("Key: " + element.getKey());
			System.out.println(element.getValue().toString()); //.replaceAll("(?m)^", "\t"));
			System.out.println("------");
		}
	}
	
	private void initializeDefault()
	{
		File data = new File("LexiconLibrary/lexiconData.txt");
		
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(data));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		LexiconWord nextWord = parseWordFromFile(reader);
		
		while (nextWord != null)
		{
			String key = nextWord.word;
			if (key == null)
			{
				System.out.println("ERROR! Key for nextWord is " + key);
			}
			else if (wordList.containsKey(key))
			{
				System.out.println("ERROR! Word " + key + " is already in HashMap<String, LexiconWord> wordList!");
			}
			else
			{
				wordList.put(key, nextWord);
			}
			nextWord = parseWordFromFile(reader);
		}
		
		
		
		try
		{
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private String readNextLine(BufferedReader reader)
	{
		String line = null;
		try
		{
			line = reader.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return line;
	}
	
	private LexiconWord parseWordFromFile(BufferedReader reader)
	{
		String currentLine = null;
		LexiconWord word = new LexiconWord();
		String currentFeature = null;
		boolean finished = false;
		
		while (finished == false)
		{
			currentLine = readNextLine(reader);
			if (currentLine == null)
			{
				finished = true;
			}
			else if (currentLine.equals("---"))
			{
				finished = true;
			}
			else
			{
				if (word.getWord() == null)
					word.setWord(currentLine); //NOTE: This *should* be just the word, no \n or \r
				else
				{
					int indentations = currentLine.length();
					currentLine = currentLine.replaceAll("\t", "");
					indentations -= currentLine.length();
					if (indentations == 0) //can't be word, must be a new type
					{
						WordType wType = new WordType(currentLine.toUpperCase());
						word.addType(wType);
					}
					else if (indentations == 1)
					{
						WordType wType = word.getLastType();
						if (wType != null)
						{
							currentFeature = currentLine.toUpperCase();
						}
						else
							System.out.println("ERROR! Tried to access latest type for word " + word.getWord() + ", but it doesn't exist!");
					}
					else if (indentations == 2)
					{
						if (currentFeature != null)
						{
							WordType wType = word.getLastType();
							if (wType != null)
							{
								wType.addFeature(currentFeature, currentLine);
							}
							else
								System.out.println("ERROR! Tried to access latest type for word " + word.getWord() + ", but it doesn't exist!");
						}
						else
							System.out.println("ERROR! Current feature is null for word " + word.getWord() + "!");
					}
					else
					{
						System.out.println("Too many indentations for word " + word.getWord() + " and currentLine = " + currentLine);
					}
					/*
					ArrayList<WordType> types = word.getTypes();
					if (types.size() == 0)
					{
						WordType wType = new WordType(currentLine.toUpperCase());
						
					}
					*/
				}
			}
			
				
		}
		
		/*
		String word;
		String type;
		ArrayList<WordFeature> features;
		*/
		
		if (currentLine == null || !currentLine.equals("---"))
		{
			System.out.println("ERROR! Failed to find end of last word.");
			word = null;
		}
		else if (word.getWord() == null)
		{
			System.out.println("ERROR! Trying to add word that isn't initialized!");
		}
		
		return word;
	}
	
	public LexiconWord getWord(String wordName)
	{
		return wordList.get(wordName);
	}
}
