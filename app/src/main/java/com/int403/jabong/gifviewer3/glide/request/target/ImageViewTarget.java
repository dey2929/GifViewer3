package com.int403.jabong.gifviewer3.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.int403.jabong.gifviewer3.glide.request.transition.Transition;

/**
 * A base {@link com.int403.jabong.gifviewer3.glide.request.target.Target} for displaying resources in {@link
 * ImageView}s.
 *
 * @param <Z> The type of resource that this target will display in the wrapped {@link
 *            ImageView}.
 */
public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z>
    implements Transition.ViewAdapter {

  @Nullable
  private Animatable animatable;

  public ImageViewTarget(ImageView view) {
    super(view);
  }

  /**
   * Returns the current {@link Drawable} being displayed in the view
   * using {@link ImageView#getDrawable()}.
   */
  @Override
  @Nullable
  public Drawable getCurrentDrawable() {
    return view.getDrawable();
  }

  /**
   * Sets the given {@link Drawable} on the view using {@link
   * ImageView#setImageDrawable(Drawable)}.
   *
   * @param drawable {@inheritDoc}
   */
  @Override
  public void setDrawable(Drawable drawable) {
    view.setImageDrawable(drawable);
  }

  /**
   * Sets the given {@link Drawable} on the view using {@link
   * ImageView#setImageDrawable(Drawable)}.
   *
   * @param placeholder {@inheritDoc}
   */
  @Override
  public void onLoadStarted(@Nullable Drawable placeholder) {
    super.onLoadStarted(placeholder);
    setResource(null);
    setDrawable(placeholder);
  }

  /**
   * Sets the given {@link Drawable} on the view using {@link
   * ImageView#setImageDrawable(Drawable)}.
   *
   * @param errorDrawable {@inheritDoc}
   */
  @Override
  public void onLoadFailed(@Nullable Drawable errorDrawable) {
    super.onLoadFailed(errorDrawable);
    setResource(null);
    setDrawable(errorDrawable);
  }

  /**
   * Sets the given {@link Drawable} on the view using {@link
   * ImageView#setImageDrawable(Drawable)}.
   *
   * @param placeholder {@inheritDoc}
   */
  @Override
  public void onLoadCleared(@Nullable Drawable placeholder) {
    super.onLoadCleared(placeholder);
    setResource(null);
    setDrawable(placeholder);
  }

  @Override
  public void onResourceReady(Z resource, @Nullable Transition<? super Z> transition) {
    if (transition == null || !transition.transition(resource, this)) {
      setResource(resource);
    }

    if (resource instanceof Animatable) {
      animatable = (Animatable) resource;
      animatable.start();
    } else {
      animatable = null;
    }
  }

  @Override
  public void onStart() {
    if (animatable != null) {
      animatable.start();
    }
  }

  @Override
  public void onStop() {
    if (animatable != null) {
      animatable.stop();
    }
  }

  protected abstract void setResource(@Nullable Z resource);
}
