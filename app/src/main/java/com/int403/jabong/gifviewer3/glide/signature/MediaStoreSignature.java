package com.int403.jabong.gifviewer3.glide.signature;

import com.int403.jabong.gifviewer3.glide.load.Key;
import com.int403.jabong.gifviewer3.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * A unique signature based on metadata data from the media store that detects common changes to
 * media store files like edits, rotations, and temporary file replacement.
 */
public class MediaStoreSignature implements Key {
  private final String mimeType;
  private final long dateModified;
  private final int orientation;

  /**
   * Constructor for {@link com.int403.jabong.gifviewer3.glide.signature.MediaStoreSignature}.
   *
   * @param mimeType     The mime type of the media store media. Ok to default to empty string "".
   *                     See {@link android.provider.MediaStore.Images.ImageColumns#MIME_TYPE} or
   *                     {@link android.provider.MediaStore.Video.VideoColumns#MIME_TYPE}.
   * @param dateModified The date modified time of the media store media. Ok to default to 0. See
   *                     {@link android.provider.MediaStore.Images.ImageColumns#DATE_MODIFIED} or
   *                     {@link android.provider.MediaStore.Video.VideoColumns#DATE_MODIFIED}.
   * @param orientation  The orientation of the media store media. Ok to default to 0. See {@link
   *                     android.provider.MediaStore.Images.ImageColumns#ORIENTATION}.
   */
  public MediaStoreSignature(String mimeType, long dateModified, int orientation) {
    this.mimeType = mimeType;
    this.dateModified = dateModified;
    this.orientation = orientation;
  }

  @SuppressWarnings({"PMD.SimplifyBooleanReturns", "RedundantIfStatement"})
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MediaStoreSignature that = (MediaStoreSignature) o;

    if (dateModified != that.dateModified) {
      return false;
    }
    if (orientation != that.orientation) {
      return false;
    }
    if (!Util.bothNullOrEqual(mimeType, that.mimeType)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = mimeType != null ? mimeType.hashCode() : 0;
    result = 31 * result + (int) (dateModified ^ (dateModified >>> 32));
    result = 31 * result + orientation;
    return result;
  }

  @Override
  public void updateDiskCacheKey(MessageDigest messageDigest) {
    byte[] data = ByteBuffer.allocate(12).putLong(dateModified).putInt(orientation).array();
    messageDigest.update(data);
    messageDigest.update(mimeType.getBytes(CHARSET));
  }
}
