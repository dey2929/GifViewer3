package com.int403.jabong.gifviewer3.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.int403.jabong.gifviewer3.glide.Glide;
import com.int403.jabong.gifviewer3.glide.load.engine.Resource;
import com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.int403.jabong.gifviewer3.glide.util.Preconditions;

/**
 * An {@link com.int403.jabong.gifviewer3.glide.load.resource.transcode.ResourceTranscoder} that converts {@link
 * Bitmap}s into {@link BitmapDrawable}s.
 */
public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {
  private final Resources resources;
  private final BitmapPool bitmapPool;

  public BitmapDrawableTranscoder(Context context) {
    this(context.getResources(), Glide.get(context).getBitmapPool());
  }

  public BitmapDrawableTranscoder(Resources resources, BitmapPool bitmapPool) {
    this.resources = Preconditions.checkNotNull(resources);
    this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
  }

  @Override
  public Resource<BitmapDrawable> transcode(Resource<Bitmap> toTranscode) {
    return LazyBitmapDrawableResource.obtain(resources, bitmapPool, toTranscode.get());
  }
}
