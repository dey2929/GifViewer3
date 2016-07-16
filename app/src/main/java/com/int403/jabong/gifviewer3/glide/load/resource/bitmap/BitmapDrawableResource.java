package com.int403.jabong.gifviewer3.glide.load.resource.bitmap;

import android.graphics.drawable.BitmapDrawable;

import com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool;
import com.int403.jabong.gifviewer3.glide.load.resource.drawable.DrawableResource;
import com.int403.jabong.gifviewer3.glide.util.Util;

/**
 * A {@link com.int403.jabong.gifviewer3.glide.load.engine.Resource} that wraps an
 * {@link BitmapDrawable}
 *
 * <p> This class ensures that every call to {@link #get()}} always returns a new
 * {@link BitmapDrawable} to avoid rendering issues if used in multiple
 * views and is also responsible for returning the underlying {@link android.graphics.Bitmap} to the
 * given {@link com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool} when the resource is
 * recycled. </p>
 */
public class BitmapDrawableResource extends DrawableResource<BitmapDrawable> {
  private final BitmapPool bitmapPool;

  public BitmapDrawableResource(BitmapDrawable drawable, BitmapPool bitmapPool) {
    super(drawable);
    this.bitmapPool = bitmapPool;
  }

  @Override
  public Class<BitmapDrawable> getResourceClass() {
    return BitmapDrawable.class;
  }

  @Override
  public int getSize() {
    return Util.getBitmapByteSize(drawable.getBitmap());
  }

  @Override
  public void recycle() {
    bitmapPool.put(drawable.getBitmap());
  }
}
