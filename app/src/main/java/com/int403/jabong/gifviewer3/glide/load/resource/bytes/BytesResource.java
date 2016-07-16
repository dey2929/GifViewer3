package com.int403.jabong.gifviewer3.glide.load.resource.bytes;

import com.int403.jabong.gifviewer3.glide.load.engine.Resource;
import com.int403.jabong.gifviewer3.glide.util.Preconditions;

/**
 * An {@link com.int403.jabong.gifviewer3.glide.load.engine.Resource} wrapping a byte array.
 */
public class BytesResource implements Resource<byte[]> {
  private final byte[] bytes;

  public BytesResource(byte[] bytes) {
    this.bytes = Preconditions.checkNotNull(bytes);
  }

  @Override
  public Class<byte[]> getResourceClass() {
    return byte[].class;
  }

  @Override
  public byte[] get() {
    return bytes;
  }

  @Override
  public int getSize() {
    return bytes.length;
  }

  @Override
  public void recycle() {
    // Do nothing.
  }
}
