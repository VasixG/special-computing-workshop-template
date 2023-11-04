package ru.spbu.apcyb.svp.tasks;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

public class StackCustomTest {

  private StackCustom<Integer> stack;

  @Before
  public void setUp() {
    stack = new StackCustom<>();
  }

  @Test
  public void testPush() {
    stack.push(1);
    stack.push(2);
    stack.push(3);
    assertEquals(3, stack.size());
  }

  @Test
  public void testPop() {
    stack.push(1);
    stack.push(2);
    stack.push(3);
    int poppedValue = stack.pop();
    assertEquals(3, poppedValue);
    assertEquals(2, stack.size());
  }

  @Test(expected = EmptyStackException.class)
  public void testPopOnEmptyStack() {
    stack.pop();
  }

  @Test
  public void testPeek() {
    stack.push(1);
    stack.push(2);
    stack.push(3);
    int peekedValue = stack.peek();
    assertEquals(3, peekedValue);
    assertEquals(3, stack.size()); // size shouldn't change after peek
  }

  @Test(expected = EmptyStackException.class)
  public void testPeekOnEmptyStack() {
    stack.peek();
  }

  @Test
  public void testEmpty() {
    assertTrue(stack.empty());
    stack.push(1);
    assertFalse(stack.empty());
  }

  @Test
  public void testSize() {
    assertEquals(0, stack.size());
    stack.push(1);
    stack.push(2);
    assertEquals(2, stack.size());
    stack.pop();
    assertEquals(1, stack.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void equalstest() {
    stack.equals(stack);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void hashCodetest() {
    stack.hashCode();
  }
}
