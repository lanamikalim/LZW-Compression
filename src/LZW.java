import java.util.*;

public class LZW
{
	public List<Integer> compress (ArrayList<Character> uncompressed)
	{
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < 256; i++)
		{
			dictionary.put("" + (char)i, i);
		}
		
		List<Integer> complete = new ArrayList<Integer>();
		
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
}
