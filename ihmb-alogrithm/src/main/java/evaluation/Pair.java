package evaluation;


public class Pair<T> {
    public T first;
    public T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object p) {
        if (p instanceof Pair<?>) {
            return (first.equals(((Pair) p).first) && second.equals(((Pair) p).second));
        }
        return false;
    }
    
    @Override
    public String toString() {
    	return first.toString() + second.toString();
    }

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode();
    }

}