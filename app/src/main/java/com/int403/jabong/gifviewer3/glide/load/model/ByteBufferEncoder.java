package com.int403.jabong.gifviewer3.glide.load.model;

import android.util.Log;

import com.int403.jabong.gifviewer3.glide.load.Encoder;
import com.int403.jabong.gifviewer3.glide.load.Options;
import com.int403.jabong.gifviewer3.glide.util.ByteBufferUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Writes {@link ByteBuffer ByteBuffers} to {@link File Files}.
 */
public class ByteBufferEncoder implements Encoder<ByteBuffer> {
  private static final String TAG = "ByteBufferEncoder";

  @Override
  public boolean encode(ByteBuffer data, File file, Options options) {
    boolean success = false;
    try {
      ByteBufferUtil.toFile(data, file);
      success = true;
    } catch (IOException e) {
      if (Log.isLoggable(TAG, Log.DEBUG)) {
        Log.d(TAG, "Failed to write data", e);
      }
    }
    return success;
  }
}
