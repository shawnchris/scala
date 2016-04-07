package concurrent2;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CountDownLatch;

public class Concurrent2 {
    private ExecutorService service;
    final private AtomicLong totalSize = new AtomicLong();
    final private AtomicLong pending = new AtomicLong();
    final private CountDownLatch latch = new CountDownLatch(1);

    private void process(final File file) {

	long total = 0;

	if (file.isFile())
	    total = file.length();
	else {
	    final File[] children = file.listFiles();
	    
	    if (children != null) {
		
		for (final File child : children) {
		    if (child.isFile())
			total += child.length();
		    else {
			pending.incrementAndGet();
			service.execute(new Runnable() { 
				public void run() { process(child);} 
			    });
		    }
		}
	    }
	}
	totalSize.addAndGet(total);
	if (pending.decrementAndGet() == 0)
	    latch.countDown();
    }

    private long startProcess(final File file) throws InterruptedException {
	service = Executors.newFixedThreadPool(100);
	pending.incrementAndGet();
	try {
	    process(file);
	    latch.await(100, TimeUnit.SECONDS);
	    return totalSize.longValue();
	} finally {
	    service.shutdown();
	}
    }

    public static void main(final String[] args) 
	throws InterruptedException {
	final long start = System.nanoTime();
	final long total = new Concurrent2().startProcess(new File(args[0]));
	final long end = System.nanoTime();
	System.out.println("Total Size: " + total);
	System.out.println("Time taken: " + (end - start)/1.0e9);
    }
}
