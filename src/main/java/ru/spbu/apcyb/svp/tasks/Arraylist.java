package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A custom implementation of a list using an array.
 *
 * @param <T> the type of elements held in this list
 */
public class Arraylist<T> implements List<T>, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private int size;
  private Object[] arr;
  private static final int INITIAL_CAPACITY = 10;

  private static final String UNSUPPORTED_MESSAGE = "Unsupported operation";

  public Arraylist() {
    this.size = 0;
    this.arr = new Object[INITIAL_CAPACITY];
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public boolean contains(Object o) {
    for (Object elem : this.arr) {
      if (o.equals(elem)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean add(Object o) {
    if (this.size == this.arr.length) {
      if ((this.size & (1 << 30)) != 0) {
        return false;
      } else {
        this.arr = Arrays.copyOf(this.arr, this.size * 2);
      }
    }

    arr[size] = o;
    ++size;
    return true;
  }

  @Override
  public void add(int index, Object element) {
    boundscheck(index);
    if (index == this.size) {
      this.add(element);
    } else {
      Object lastel = this.arr[this.size - 1];
      System.arraycopy(this.arr, index, this.arr, index + 1, size - index - 1);
      this.arr[index] = element;
      this.add(lastel);
    }
  }

  @Override
  public T remove(int index) {
    boundscheck(index);
    Object elem = this.arr[index];
    System.arraycopy(this.arr, index + 1, this.arr, index, size - index - 1);
    size -= 1;
    return (T) elem;
  }

  @Override
  public boolean remove(Object o) {
    for (int i = 0; i < size; ++i) {
      if (this.arr[i].equals(o)) {
        this.remove(i);
      }
    }
    return true;
  }

  @Override
  public T get(int index) {
    boundscheck(index);
    return (T) this.arr[index];
  }

  @Override
  public T set(int index, Object element) {
    boundscheck(index);
    Object temp = this.arr[index];
    this.arr[index] = element;
    return (T) temp;
  }

  @Override
  public void clear() {
    this.arr = new Object[0];
    this.size = 0;
  }

  @Override
  public <E> E[] toArray(E[] a) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public Iterator<T> iterator() {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public T[] toArray() {
    return Arrays.copyOf(this.arr, size, (Class<T[]>) this.arr.getClass());
  }

  @Override
  public boolean addAll(Collection c) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public boolean addAll(int index, Collection c) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public int indexOf(Object o) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public int lastIndexOf(Object o) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public ListIterator<T> listIterator() {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public boolean retainAll(Collection c) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public boolean removeAll(Collection c) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public boolean containsAll(Collection c) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  /**
   * Checks if the provided index is within the valid range for the list.
   *
   * @param index the index to check
   * @throws IndexOutOfBoundsException if the index is out of the valid range
   */
  public void boundscheck(int index) {
    if (index < 0 || index > this.size) {
      throw new IndexOutOfBoundsException("Index out of bound");
    }
  }

  @Serial
  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    out.writeInt(this.size);
    for (int i = 0; i < this.size; i++) {
      out.writeObject(this.arr[i]);
    }
  }

  @Serial
  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    size = in.readInt();
    arr = new Object[size];
    for (int i = 0; i < size; i++) {
      arr[i] = in.readObject();
    }
  }


}
