public class ThreadPoolTest
{
    private ThreadPool pool = new ThreadPool(2);

    public static void main(String[] args)
    {
	ThreadPoolTest test = new ThreadPoolTest();
	test.startRunnable("TASK 1");
	test.startRunnable("TASK 2");
	test.startRunnable("TASK 3");
	test.startRunnable("TASK 4");
	    
	try {
	    Thread.currentThread().sleep(4500);
	}
	catch ( InterruptedException ex ) {}
    }

    private void startRunnable(final String id)
    {
	pool.execute(
		     new Runnable()
		     {
			 public void run()
			 {
			     System.out.println("Start " +
						Thread.currentThread().getName()
						+ " " + id);
			     try {
				 Thread.currentThread().sleep(500);
			     }
			     catch ( InterruptedException ex ) {}
			     System.out.println("Stop  " +
						Thread.currentThread().getName()
						+ " " + id);
			 }
		     }
		     );
    }
}
