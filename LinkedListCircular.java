package util;

import java.util.*;
import java.util.function.Consumer;

public class LinkedListCircular<E> implements ListIterator<E>
{
  Node<E> current;
  int size;
  int index;

  public LinkedListCircular(Collection<? extends E> c)
  {
    this((E[])c.toArray());
  }

  public LinkedListCircular(E... values)
  {
    this();
    for (E v : values)
    {
      add(v);
      size++;
    }
  }

  public LinkedListCircular()
  {
    current = null;
    size = 0;
    index = 0;
  }

  public int size()
  {
    return size;
  }

  public E get()
  {
    return current.item;
  }

  public void set(E element)
  {
    //E cur = current.item;
    current.item = element;
    //return cur;
  }

  public void add(E e)
  {
    if (size !=0)
    {
      size++;
      current.next.prev = new Node<E>(current, e, current.next);
      current.next = current.next.prev;
    }
    else
    {
      current = new Node<E>(null,e,null);
      current.next=current;
      current.prev=current;
      size++;
    }
    fix();
  }

  public void remove()
  {
    if (size==1)
    {
      current = null;
    }
    else
    {
      current.prev.next = current.next;
      current.next.prev = current.prev;
      current = current.next;
    }
    size--;
    fix();
  }

  @Override
  public boolean hasNext()
  {
    return true;
  }

  public E next()
  {
    E cur = current.item;
    current = current.next;
    index = index==size()-1?0:index+1;
    fix();
    return cur;
  }

  @Override
  public boolean hasPrevious()
  {
    return true;
  }

  @Override
  public E previous()
  {
    current = current.prev;
    index = index==0?size()-1:index-1;
    fix();
    return current.item;
  }

  @Override
  public int nextIndex()
  {
    return index;
  }

  @Override
  public int previousIndex()
  {
    return index==0?size()-1:index-1;
  }

  private static class Node<E>
  {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next)
    {
      this.item = element;
      this.next = next;
      this.prev = prev;
    }
  }

  public Object[] toArray()
  {
    Object[] ret = new Object[size()];
    int old = index;
    while (nextIndex()!=0)
      previous();
    for (int i = 0; i<size(); i++)
      ret[i] = next();
    while (index!=old)
      next();
    return ret;
  }

  private void fix()
  {
    if (size==0)
      current = null;
    if (size==0)
      index=0;
    else
    {
      while (index >= size)
        index -= size;
      while (index < 0)
        index += size;
    }
  }

  public String toString()
  {
    return get().toString();
  }
}
