public class ProducerConsumerTest
{
    public static void main( String args[] )
    {
	final UnsyncBuffer buffer = new UnsyncBuffer();
	//final SyncBuffer buffer = new SyncBuffer();

	// Producer
	Runnable p = new Runnable() {
	    public void run() {
		for ( int count = 1; count <= 10; count++ ) {
		    try {
			Thread.sleep( (int) ( Math.random() * 300 ) );
		    }
		    catch( InterruptedException e ) {
			System.out.println( e.toString() );
		    }
		    buffer.setSharedInt( count );
		}
		System.out.println( Thread.currentThread().getName() +
				    " finished producing values" +
				    "\nTerminating " + 
				    Thread.currentThread().getName() );
	    }};

	// Consumer
	Runnable c = new Runnable() {
	    public void run() {
		int val, sum = 0;
		do {
		    try {
			Thread.sleep( (int) ( Math.random() * 300 ) );
		    }
		    catch( InterruptedException e ) {
			System.out.println( e.toString() );
		    }
		    val = buffer.getSharedInt();
		    sum += val;
		} 
		while ( val < 10  );

		System.out.println(Thread.currentThread().getName() + 
				   " retrieved values totaling: " + 
				   sum + 
				   "\nTerminating " + 
				   Thread.currentThread().getName() );
	    }};

	new Thread(p, "Producer").start();
	new Thread(c, "Consumer").start();
    }
}
