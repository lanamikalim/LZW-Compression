import java.io.IOException;
import java.util.ArrayList;

public class Tester
{
	public static void main(String[] args) throws IOException
	{
		LZW compressor = new LZW ();
		
		ArrayList<Character> charList = compressor.makeArrayList("lzw-file1.txt");
		ArrayList<Integer> intList = compressor.compress(charList);
		System.out.println (intList);
		String word = compressor.decompress(intList);
		System.out.println (word);
	}
}
