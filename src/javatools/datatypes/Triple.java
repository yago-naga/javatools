package javatools.datatypes;

public class Triple<F, S, T> extends Pair<F, S> {

  /** Holds the second component */
  private T third;

  /** Returns the second */
  public T third() {
    return third;
  }

  /** Sets the second */
  public void setThird(T third) {
    this.third = third;
  }

  /** Constructs a Pair*/
  public Triple(F first, S second, T third) {
    super(first, second);
    this.third = third;
  }

  public int hashCode() {
    return (super.hashCode() ^ third.hashCode());
  }

  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    return (obj instanceof Triple) && ((Triple) obj).first().equals(first) && ((Triple) obj).second().equals(second)
        && ((Triple) obj).third().equals(third);
  }

  /** Returns "first/second"*/
  public String toString() {
    return first + "/" + second +"/" +third;
  }
}
