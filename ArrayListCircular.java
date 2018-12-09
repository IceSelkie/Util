package util;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListCircular<E> extends java.util.ArrayList<E>
{
  public ArrayListCircular(int initialCapacity)
  {
    super(initialCapacity);
  }

  public ArrayListCircular(Collection<? extends E> c)
  {
    super(c);
  }

  /*public ArrayListCircular(E[] values)
  {
    this(values.length);
    for (E value : values)
      this.add(value);
  }*/
  public ArrayListCircular(E... values)
  {
    this(values.length);
    for (E value : values)
      this.add(value);
  }

  public ArrayListCircular()
  {
    super();
  }

  @Override
  public E get(int index) {
    if (size()==0)
      return super.get(0);
    index = (index % size() + size()) % size();
    return super.get(index%super.size());
  }
  @Override
  public E set(int index, E element) {
    index = index%size();
    return super.set(index,element);
  }


  public void reverse()
  {
    reverse(0,size()-1);
  }
  public void reverse(int begin, int end)
  {
    if (begin>=size())
      begin = begin%size();
    if (end>=size())
      end = end%size();
    if (end<begin)
      end = end+size();
    if (begin==end)
      return;
    ArrayListCircular<E> temp = new ArrayListCircular<E>(end-begin+1);
    for (int loc = end; loc>=begin; loc--)
      temp.add(this.get(loc));
    for (int loc = begin; loc<=end; loc++)
      this.set(loc,temp.get(loc-begin));
  }


  public void add(int index, E element)
  {
    if (size()==0 || (index != 0 && index % size() == 0))
      add(element);
    else
    {
      index = (index % size() + size()) % size();
      super.add(index, element);
    }
  }


  public E remove(int index)
  {
    index = (index % size() + size()) % size();
    return super.remove(index);
  }
}
