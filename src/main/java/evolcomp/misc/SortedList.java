package evolcomp.misc;

import java.util.Comparator;
import java.util.LinkedList;

public class SortedList<E> extends LinkedList<E>
{
    private final Comparator<E> comparator;

    public SortedList(final Comparator<E> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public void add(int index, E element)
    {
        this.add(element);
    }

    @Override
    public boolean add(final E e)
    {
        final boolean result = super.add(e);
        this.sort(this.comparator);
        return result;
    }
}