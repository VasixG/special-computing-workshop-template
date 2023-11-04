package ru.spbu.apcyb.svp.tasks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArraylistTest {

  private Arraylist<Integer> arraylist;

  @Before
  public void setUp() {
    arraylist = new Arraylist<>();
  }

  @Test
  public void testAdd() {
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(1);
    arraylist.add(2);
    arraylist.set(1, 3);
    assertEquals(Integer.valueOf(2), arraylist.get(15));
    assertEquals(16, arraylist.size());
    assertArrayEquals(new Integer[]{1, 3, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2},
        arraylist.toArray());
  }

  @Test
  public void testAddWithIndex() {
    arraylist.add(0, 1);
    arraylist.add(1, 2);
    arraylist.add(1, 3);
    assertEquals(3, arraylist.size());
    assertArrayEquals(new Integer[]{1, 3, 2}, arraylist.toArray());
  }

  @Test
  public void testRemove() {
    arraylist.add(1);
    arraylist.add(2);
    arraylist.remove(0);
    assertEquals(1, arraylist.size());
    assertArrayEquals(new Integer[]{2}, arraylist.toArray());
  }

  @Test
  public void testRemoveObject() {
    arraylist.add(1);
    arraylist.add(2);
    arraylist.remove(Integer.valueOf(1));
    assertEquals(1, arraylist.size());
    assertArrayEquals(new Integer[]{2}, arraylist.toArray());
  }

  @Test
  public void testContains() {
    arraylist.add(1);
    assertTrue(arraylist.contains(1));
    assertFalse(arraylist.contains(2));
  }

  @Test
  public void testIsEmpty() {
    assertTrue(arraylist.isEmpty());
    arraylist.add(1);
    assertFalse(arraylist.isEmpty());
  }

  @Test
  public void testClear() {
    arraylist.add(1);
    arraylist.add(2);
    arraylist.clear();
    assertArrayEquals(new Integer[]{}, arraylist.toArray());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOutOfBounds() {
    arraylist.add(1);
    arraylist.get(2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testToArrayWithArg() {
    Integer[] array = new Integer[5];
    arraylist.toArray(array);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIterator() {
    arraylist.iterator();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    arraylist.addAll(collection);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAllWithIndex() {
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    arraylist.addAll(1, collection);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIndexOf() {
    arraylist.indexOf(1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testLastIndexOf() {
    arraylist.lastIndexOf(1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testListIterator() {
    arraylist.listIterator();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testListIteratorWithIndex() {
    arraylist.listIterator(1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSubList() {
    arraylist.subList(0, 1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRetainAll() {
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    arraylist.retainAll(collection);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveAll() {
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    arraylist.removeAll(collection);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testContainsAll() {
    Collection<Integer> collection = Arrays.asList(1, 2, 3);
    arraylist.containsAll(collection);
  }

  @Test
  public void testSerialization() throws IOException, ClassNotFoundException, IOException {
    // Add some elements to the list
    arraylist.add(1);
    arraylist.add(2);
    arraylist.add(3);

    // Serialize
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(arraylist);
    out.flush();
    byte[] data = bos.toByteArray();

    // Deserialize
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    ObjectInputStream in = new ObjectInputStream(bis);
    Arraylist<Integer> deserializedList = (Arraylist<Integer>) in.readObject();

    // Verify the deserialized list's content
    assertEquals(3, deserializedList.size());
    assertEquals(Integer.valueOf(1), deserializedList.get(0));
    assertEquals(Integer.valueOf(2), deserializedList.get(1));
    assertEquals(Integer.valueOf(3), deserializedList.get(2));
  }

}
