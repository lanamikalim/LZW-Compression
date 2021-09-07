import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LZW
{
	//method which turns file into ArrayList of chars
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
	
	public ArrayList<Integer> compress (ArrayList<Character> uncompressed)
	{
		//creating the dictionary and filling it with all the chars
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < 256; i++)
		{
			dictionary.put("" + (char)i, i);
		}
		
		//create an ArrayList of ints and add to it as I compress the file
		ArrayList<Integer> complete = new ArrayList<Integer>();
		
		String currentLetters = "";

		//go through the ArrayLists of chars
		
		for (int i = 0; i < uncompressed.size(); i++)
		{
			char theChar = uncompressed.get(i);
			
			String theString = currentLetters + theChar;
			
			//if the dictionary already contains the String in its directory, set the current set of letters to the String
			//if the dictionary doesn't contain the String in its directory, add the new int to the compressed output, add it in the dictionary, and reset currentLetters
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
		
		//if currentLetters isn't empty, add the compressed int to the ArrayList
		if (!currentLetters.equals(""))
		{
			complete.add(dictionary.get(currentLetters));
		}
		
		//return compressed list of ints
		return (complete);
	}
	
	public String decompress (ArrayList<Integer> compressed)
	{
		//creating the dictionary and filling it with all the chars
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < 256; i++)
		{
			dictionary.put(i, "" + (char)i);
		}
		
		//extract the first char & initialize it to a variable
		int firstCompressedInt = compressed.get(0);
		Character firstCompressedChar = (char)firstCompressedInt;
		
		//remove the first compressed part so it doesn't repeat in output
		compressed.remove(0);
		
		//initialize the firstCompressedChar as a String
		String currentLetters = "" + firstCompressedChar;
		
		//prepare the output String
		String output = currentLetters;
		
		//go through compressed ArrayList one-by-one
		for (int i = 0; i < compressed.size(); i++)
		{
			int letters = compressed.get(i);
					
			String dictionaryEntry;
			
			//if dictionary already contains the int in its directory, initialize the corresponding chars to dictionaryEntry
			//if dictionary does not contain the int in its directory, add the first char to the currentLetters
			if (dictionary.containsKey(letters))
			{
				dictionaryEntry = dictionary.get(letters);
			}
			else
			{
				dictionaryEntry = currentLetters + currentLetters.charAt(0);
			}
			
			//add the entry to the output
			output = output + dictionaryEntry;
			
			//add new char combination to the dictionary
			dictionary.put(dictionarySize++, currentLetters + dictionaryEntry.charAt(0));
			
			
			//reset current letters with the new entry
			currentLetters = dictionaryEntry;
		}
		
		//return uncompressed String
		return (output);
	}
}