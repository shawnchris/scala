public class SyncBuffer 
{
    private int sharedInt = -1;
    private boolean writeable = true;  // condition variable

    public synchronized void setSharedInt( int val )
    {
	while ( !writeable ) {  // not the producer's turn
	    try {
		wait();  
	    }
	    catch ( InterruptedException e ) {
		e.printStackTrace();
	    }
	}

	System.out.println( Thread.currentThread().getName() +
			  " setting sharedInt to " + val );
	sharedInt = val;

	writeable = false;
	notify();  // tell a waiting thread to become ready
    }

    public synchronized int getSharedInt()
    {
	while ( writeable ) {   // not the consumer's turn
	    try {
		wait();
	    }
	    catch ( InterruptedException e ) {
		e.printStackTrace();
	    }
	}

	writeable = true;

	System.out.println( Thread.currentThread().getName() +
			    " retrieving sharedInt value " + sharedInt );

	notify();  // tell a waiting thread to become ready
	return sharedInt;
    }
}

