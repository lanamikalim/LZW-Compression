import java.io.IOException;
import java.util.ArrayList;

public class Tester
{
	public static void main(String[] args) throws Exception
	{
		LZW compressor = new LZW ();

		compressor.compress("lzw-file3.txt", "output.dat");
	}
}
