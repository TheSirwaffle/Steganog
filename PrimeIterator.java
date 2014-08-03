import java.util.ArrayList;
import java.util.Iterator;


public class PrimeIterator implements Iterator<Integer> {

	private int maximumValue;
	private int currentIndex;
	private boolean[] primes;
	
	public PrimeIterator(int max)
	{
		currentIndex = 2;
		maximumValue = max;
		primes = new boolean[maximumValue+1];
		for(int i=0; i<primes.length; i++)
		{
			primes[i] = true;
		}
	}
	
	private void crossoutPrimes(int start)
	{
		int currentValue = start*start;
		while(currentValue < primes.length && currentValue > 0)
		{
			primes[currentValue] = false;
			currentValue += start;
		}
	}
	
	private void getNextIndex()
	{
		boolean found = false;
		for(int i=currentIndex+1; i<primes.length && !found; i++)
		{
			found = primes[i];
			if(found)
			{
				currentIndex = i;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		boolean found = false;
		for(int i=currentIndex+1; i<primes.length && !found; i++)
		{
			found = primes[i];
		}
		return found;
	}

	@Override
	public Integer next() {
		Integer returnValue = currentIndex;
		crossoutPrimes(currentIndex);
		getNextIndex();
		return returnValue;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
