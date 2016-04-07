public class ThreadPool
{
    private BlockingQueue<Runnable> taskQueue = new BlockingQueue<Runnable>();

    public ThreadPool(int numThreads)
    {
	for(int i = 0; i < numThreads; i++) {

	    Runnable r = new Runnable() {
		    public void run() {
			while (true) {
			    try {
				(taskQueue.dequeue()).run();
			    }
			    catch ( InterruptedException ex ) {
				// do nothing
			    }
			}
		    }
		};

	    new Thread(r).start();
	}
    }

    public synchronized void execute (Runnable task)
    {
	taskQueue.enqueue(task);
    }
}
