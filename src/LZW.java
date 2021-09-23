import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
			System.out.print(" int to: " + a + " in binary is: " + intToBinaryString);
			if (intToBinaryString.length() == bitLength)
			{
				System.out.println("");
				output = output + Integer.toBinaryString(a);
			}
			else if (intToBinaryString.length() < bitLength)
			{
				intToBinaryString = String.format("%" + bitLength + "s", Integer.toBinaryString(a)).replaceAll(" ", "0");
				System.out.print(" padded to: " + intToBinaryString + "\n");
				output = output + intToBinaryString;
			}
			else
			{
				throw new Exception("File is unable to be compressed using a " + bitLength + " bit compressor.");
			}
		}
		System.out.println("output: " + output);
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
	public void decompress(String filename, String outputFilename) throws IOException
	{
		
      InputStream reader = new FileInputStream(filename);
      BinaryIn binaryIn = new BinaryIn(reader);
      
      
      StringBuilder dataBuilder = new StringBuilder();
      try {
    	  while(!binaryIn.isEmpty()) {
    		  boolean bit = binaryIn.readBoolean();
    		  if(bit) {
    			  dataBuilder.append("1");
    		  } else {
    			  dataBuilder.append("0");
    		  }
    	  }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
   System.out.println(" data builder: " + dataBuilder.toString()); 
   String finalData = dataBuilder.toString();
   String cleanData = finalData.substring(0, finalData.length());

	ArrayList<String> twelveBitPieces = new ArrayList<String>();
	for(int i =0; i < cleanData.length()-12 ; i+=12) {


		twelveBitPieces.add(cleanData.substring(i,i+12));
	
	}
	
	List<Integer> intList = new ArrayList<Integer>();
	for(String s: twelveBitPieces) {
		Integer newInt = Integer.parseInt(s,2);
		intList.add(newInt);
	}
		String finalDecompressedString = decompressFromInput(intList);
		writeBytesToFile(finalDecompressedString.getBytes(), outputFilename);
		reader.close();
	
	}
	
	
	public void writeBytesToFile(byte[] resultInBytes, String fileName) {
		File outputFile =  new File(fileName);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			System.out.println("byte results:" + resultInBytes[0]);
			outputStream.write(resultInBytes);
		} catch (IOException e) {
			System.out.print(String.format(" Cannot read the file: %s", fileName));
		}
	}
	
	public String decompressFromInput(List<Integer> compressedInput) {

		int dictionarySize = 256;
		Map<Integer,String> dictionary = buildDictionaryForDecompression(dictionarySize);

		String word = "" + (char)(int)compressedInput.get(0);

		StringBuffer result = new StringBuffer(word);

		for (int i = 1; i < compressedInput.size()-1; i++) {
			String entry;
			int digit = compressedInput.get(i);

			if (dictionary.containsKey(digit)) {
				entry =  dictionary.get(digit);
			}
			else if (digit == dictionarySize) {
				entry = word + word.charAt(0);
			} 
			else {
				throw new IllegalArgumentException("Bad compressed digit: " + digit);
			}

			result.append(entry);

			dictionary.put(dictionarySize++, word + entry.charAt(0));

			word = entry;
		}

		return result.toString();
	}
	
	private Map<Integer,String> buildDictionaryForDecompression(int dictionarySize) {
		Map<Integer,String> dictionary = new HashMap<Integer,String>();
		for (int i = 0; i < dictionarySize; i++) {
			dictionary.put(i, "" + (char)i);
		}
		return dictionary;
	}
}
