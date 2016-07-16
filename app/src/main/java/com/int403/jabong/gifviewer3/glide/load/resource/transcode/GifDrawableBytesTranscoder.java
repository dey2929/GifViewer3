package com.int403.jabong.gifviewer3.glide.load.resource.transcode;

import com.int403.jabong.gifviewer3.glide.load.engine.Resource;
import com.int403.jabong.gifviewer3.glide.load.resource.bytes.BytesResource;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawable;
import com.int403.jabong.gifviewer3.glide.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * An {@link com.int403.jabong.gifviewer3.glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawable} into bytes by obtaining the original bytes of
 * the GIF from the {@link com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
  @Override
  public Resource<byte[]> transcode(Resource<GifDrawable> toTranscode) {
    GifDrawable gifData = toTranscode.get();
    ByteBuffer byteBuffer = gifData.getBuffer();
    return new BytesResource(ByteBufferUtil.toBytes(byteBuffer));
  }
}
