package klondike;

import java.io.IOException;

/**
 * A mock Appendable that throws an IOException on any append attempt.
 * Used to test controller error handling when output fails.
 */
public class MockAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Append failed intentionally");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Append failed intentionally");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Append failed intentionally");
  }
}
