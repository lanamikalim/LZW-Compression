import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LZW
{
	public ArrayList<Integer> compress (ArrayList<Character> uncompressed)
	{
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < 256; i++)
		{
			dictionary.put("" + (char)i, i);
		}
		
		ArrayList<Integer> complete = new ArrayList<Integer>();
		
		String currentLetters = "";

		for (char theChar : uncompressed)
		{
			String theString = currentLetters + theChar;
			
			if (dictionary.containsKey(theString))
			{
				currentLetters = theString;
			}
			else
			{
				complete.add(dictionary.get(currentLetters));
				dictionary.put(theString, dictionarySize++);
				currentLetters = "" + theChar;
			}
		}
		
		if (!currentLetters.equals(""))
		{
			complete.add(dictionary.get(currentLetters));
		}
		
		return (complete);
	}
	
	public String decompress (ArrayList<Integer> compressed)
	{
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < 256; i++)
		{
			dictionary.put(i, "" + (char)i);
		}
		
		String currentLetters = "" + (char)(int)compressed.remove(0);
		
		for (int letters : compressed)
		{
			String dictionaryEntry;
			
			if (dictionary.containsKey(letters))
			{
				dictionaryEntry = dictionary.get(letters);
			}
			else
			{
				dictionaryEntry = currentLetters + currentLetters.charAt(0);
			}
			
			currentLetters = currentLetters + dictionaryEntry;
			
			dictionary.put(dictionarySize++, currentLetters + dictionaryEntry.charAt(0));
			
			currentLetters =  dictionaryEntry;
		}
		
		return (currentLetters);
	}
	
	public ArrayList<Character> makeArrayList (String fileName) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		ArrayList<Character> list = new ArrayList<Character>();
		
		while (reader.ready())
		{
			list.add((char)reader.read());
		}
		
		return (list);
	}
}
