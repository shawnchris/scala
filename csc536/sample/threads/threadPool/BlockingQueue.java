import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BlockingQueue<E>
{
    private LinkedList<E> elements = new LinkedList<E>();

    public synchronized final void enqueue(E elem)
    {
	elements.addLast(elem);
	notify();
    }

    public synchronized final E dequeue() throws InterruptedException
    {
	try {
	    while ( elements.size() <= 0 ) {
		wait();
	    }
	    return elements.removeFirst();
	}
	catch ( NoSuchElementException ex ) {
	    throw new Error("Internal Error in BlockingQueue");
	}
    }

    public synchronized final boolean isEmpty()
    {
	return elements.size() <= 0;
    }
}
