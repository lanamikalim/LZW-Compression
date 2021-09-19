import java.io.IOException;
import java.util.ArrayList;

public class Tester
{
	public static void main(String[] args)
	{
		LZW compressor = new LZW ();

		try {
			compressor.compress("lzw-file3.txt", "output.dat");
			compressor.decompress("output.dat", "decompressedOutput.txt");
		} catch (IOException e) {
			System.out.println("IOEXCEPTION THROWN, " + e.getMessage());
			
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("EXCEPTION THROWN, " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
