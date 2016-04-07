package sequential;

import java.io.File;

public class Sequential {

    public static long process(File path) {
	if (path.isFile())
	    return path.length();
        long total = 0;
	File[] items = path.listFiles();
	if (items != null)
	    for (File f: items)
		total += process(f);
	return total;
    }

    public static void main(String[] args) {
	File f = new File(args[0]);
        final long start = System.nanoTime(); 
        final long total = process(f);
        final long end = System.nanoTime();
	System.out.println("Total size: " + total);
        System.out.println("Time taken: " + (end-start)/1.0e9);
    }
}
