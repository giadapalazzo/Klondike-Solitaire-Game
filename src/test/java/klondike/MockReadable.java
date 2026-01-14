package klondike;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * A mock Readable that throws an IOException on read attempts.
 * Used to test controller input failure handling.
 */
public class MockReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException(" Input stream failed intentionally");
  }

}
