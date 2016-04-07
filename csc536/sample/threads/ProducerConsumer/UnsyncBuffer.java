public class UnsyncBuffer
{
    private int sharedInt = -1;

    public void setSharedInt( int val )
    {
	System.out.println( Thread.currentThread().getName() +
			    " setting sharedInt to " + val );
	sharedInt = val;
    }

    public int getSharedInt()
    {
	System.out.println( Thread.currentThread().getName() +
			    " retrieving sharedInt value " + sharedInt );
	return sharedInt;
    }
}
