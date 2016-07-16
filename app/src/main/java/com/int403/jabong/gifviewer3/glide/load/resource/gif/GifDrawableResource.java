package com.int403.jabong.gifviewer3.glide.load.resource.gif;

import com.int403.jabong.gifviewer3.glide.load.resource.drawable.DrawableResource;

/**
 * A resource wrapping an {@link com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableResource extends DrawableResource<GifDrawable> {
  public GifDrawableResource(GifDrawable drawable) {
    super(drawable);
  }

  @Override
  public Class<GifDrawable> getResourceClass() {
    return GifDrawable.class;
  }

  @Override
  public int getSize() {
   return drawable.getSize();
  }

  @Override
  public void recycle() {
    drawable.stop();
    drawable.recycle();
  }
}
