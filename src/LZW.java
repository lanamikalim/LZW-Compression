import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	public void compress (String inputFileName, String outputFileName) throws Exception
	{
		ArrayList<Character> uncompressed = makeArrayList(inputFileName);
		
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		
		int dictionarySize = 256;
		
		for (int i = 0; i < dictionarySize; i++)
		{
			Character theChar = (char)i;
			String theString = "" + theChar;
			dictionary.put(theString, i);
		}
		
		ArrayList<Integer> complete = new ArrayList<Integer>();
		
		String currentLetters = "";
		
		for (int i = 0; i < uncompressed.size(); i++)
		{
			char theChar = uncompressed.get(i);
			
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
		
		makeCompressedFile (complete, outputFileName);
	}
	
	public void makeCompressedFile (ArrayList<Integer> compressed, String fileName) throws Exception
	{
		String output = "";
		int bitLength = 12;
		
		for (int a : compressed)
		{
			String intToBinaryString = Integer.toBinaryString(a);
			
			if (intToBinaryString.length() == bitLength)
			{
				output = output + Integer.toBinaryString(a);
			}
			else if (intToBinaryString.length() < bitLength)
			{
				intToBinaryString = String.format("%" + bitLength + "s", Integer.toBinaryString(a)).replaceAll(" ", "0");
				output = output + Integer.toBinaryString(a);
			}
			else
			{
				throw new Exception("File is unable to be compressed using a " + bitLength + " bit compressor.");
			}
		}
		
		BinaryOut theOutClass = new BinaryOut(fileName);
       
		for (int i = 0; i < output.length(); i++)
		{
            if (output.charAt(i) == '0')
            {
                theOutClass.write(false);
            }
            else
            {
                theOutClass.write(true);
            }
        }
       
		theOutClass.flush();
	}
	public void decompress(String filename, String outputFilename)
	{
		PrintWriter output = new PrintWriter(outputFilename);
		InputStream reader = new InputStream(filename);
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		int dictionarySize = 256;
		for (int i = 0; i < dictionarySize; i++)
		{
			Character theChar = (char)i;
			String theString = "" + theChar;
			dictionary.put(i,theString);
		}
		StringBuilder newBytes = new StringBuilder();
		int byteRead = reader.read();
		int bitLen = 12;
        while (byteRead != -1) 
         {
        	String byteStr = byteRead.toBinaryString();
        	while(byteStr.length()<bitLen)
        	{
        		byteStr="0"+byteStr;
        	}
        	newBytes.append(byteStr);
         }
        
        for(int i=0;i<newBytes.length();i+=bitLen)
        {
        	int current = (int)(newBytes.substring(i,i+bitLen));
        	int next = (int)(newBytes.substring(i+bitLen,i+bitLen+bitLen));
        	if(dictionary.containsValue(dictionary.getValue(current)+dictionary.getValue(next).toString(0,1)))
        	{
        		dictionary.put(dictionary.size(), dictionary.getValue(current)+dictionary.getValue(next).toString(0,1));
        	}
        }
        for(int i=0;i<newBytes.length();i+=bitLen)
        {
        	int current = (int)(newBytes.substring(i,i+bitLen));
        	output.print(dictionary.get(current));
        }
        reader.close();
        out.close();
	}
}
