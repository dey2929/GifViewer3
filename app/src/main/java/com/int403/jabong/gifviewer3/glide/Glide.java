package com.int403.jabong.gifviewer3.glide;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.int403.jabong.gifviewer3.glide.gifdecoder.GifDecoder;
import com.int403.jabong.gifviewer3.glide.load.DecodeFormat;
import com.int403.jabong.gifviewer3.glide.load.data.InputStreamRewinder;
import com.int403.jabong.gifviewer3.glide.load.engine.Engine;
import com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.ArrayPool;
import com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool;
import com.int403.jabong.gifviewer3.glide.load.engine.cache.MemoryCache;
import com.int403.jabong.gifviewer3.glide.load.engine.prefill.BitmapPreFiller;
import com.int403.jabong.gifviewer3.glide.load.engine.prefill.PreFillType;
import com.int403.jabong.gifviewer3.glide.load.model.AssetUriLoader;
import com.int403.jabong.gifviewer3.glide.load.model.ByteArrayLoader;
import com.int403.jabong.gifviewer3.glide.load.model.ByteBufferEncoder;
import com.int403.jabong.gifviewer3.glide.load.model.ByteBufferFileLoader;
import com.int403.jabong.gifviewer3.glide.load.model.DataUrlLoader;
import com.int403.jabong.gifviewer3.glide.load.model.FileLoader;
import com.int403.jabong.gifviewer3.glide.load.model.GlideUrl;
import com.int403.jabong.gifviewer3.glide.load.model.MediaStoreFileLoader;
import com.int403.jabong.gifviewer3.glide.load.model.ResourceLoader;
import com.int403.jabong.gifviewer3.glide.load.model.StreamEncoder;
import com.int403.jabong.gifviewer3.glide.load.model.StringLoader;
import com.int403.jabong.gifviewer3.glide.load.model.UnitModelLoader;
import com.int403.jabong.gifviewer3.glide.load.model.UriLoader;
import com.int403.jabong.gifviewer3.glide.load.model.UrlUriLoader;
import com.int403.jabong.gifviewer3.glide.load.model.stream.HttpGlideUrlLoader;
import com.int403.jabong.gifviewer3.glide.load.model.stream.HttpUriLoader;
import com.int403.jabong.gifviewer3.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.int403.jabong.gifviewer3.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.int403.jabong.gifviewer3.glide.load.model.stream.UrlLoader;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.BitmapEncoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.Downsampler;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.bytes.ByteBufferRewinder;
import com.int403.jabong.gifviewer3.glide.load.resource.file.FileDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.ByteBufferGifDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawable;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.GifDrawableEncoder;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.GifFrameResourceDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.gif.StreamGifDecoder;
import com.int403.jabong.gifviewer3.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.int403.jabong.gifviewer3.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.int403.jabong.gifviewer3.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.int403.jabong.gifviewer3.glide.manager.ConnectivityMonitorFactory;
import com.int403.jabong.gifviewer3.glide.manager.RequestManagerRetriever;
import com.int403.jabong.gifviewer3.glide.module.GlideModule;
import com.int403.jabong.gifviewer3.glide.module.ManifestParser;
import com.int403.jabong.gifviewer3.glide.request.RequestOptions;
import com.int403.jabong.gifviewer3.glide.request.target.ImageViewTargetFactory;
import com.int403.jabong.gifviewer3.glide.request.target.Target;
import com.int403.jabong.gifviewer3.glide.util.Util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A singleton to present a simple static interface for building requests with
 * {@link RequestBuilder} and maintaining an {@link Engine}, {@link BitmapPool},
 * {@link com.int403.jabong.gifviewer3.glide.load.engine.cache.DiskCache} and {@link MemoryCache}.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Glide implements ComponentCallbacks2 {
  private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
  private static final String TAG = "Glide";
  private static volatile Glide glide;

  private final Engine engine;
  private final BitmapPool bitmapPool;
  private final MemoryCache memoryCache;
  private final BitmapPreFiller bitmapPreFiller;
  private final GlideContext glideContext;
  private final Registry registry;
  private final ArrayPool arrayPool;
  private final ConnectivityMonitorFactory connectivityMonitorFactory;
  private final List<RequestManager> managers = new ArrayList<>();

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context A context.
   * @see #getPhotoCacheDir(Context, String)
   */
  @Nullable
  public static File getPhotoCacheDir(Context context) {
    return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context   A context.
   * @param cacheName The name of the subdirectory in which to store the cache.
   * @see #getPhotoCacheDir(Context)
   */
  @Nullable
  public static File getPhotoCacheDir(Context context, String cacheName) {
    File cacheDir = context.getCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }

  /**
   * Get the singleton.
   *
   * @return the singleton
   */
  public static Glide get(Context context) {
    if (glide == null) {
      synchronized (Glide.class) {
        if (glide == null) {
          Context applicationContext = context.getApplicationContext();
          List<GlideModule> modules = new ManifestParser(applicationContext).parse();

          GlideBuilder builder = new GlideBuilder(applicationContext);
          for (GlideModule module : modules) {
            module.applyOptions(applicationContext, builder);
          }
          glide = builder.createGlide();
          for (GlideModule module : modules) {
            module.registerComponents(applicationContext, glide.registry);
          }
        }
      }
    }

    return glide;
  }

  @VisibleForTesting
  public static void tearDown() {
    glide = null;
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  Glide(
      Context context,
      Engine engine,
      MemoryCache memoryCache,
      BitmapPool bitmapPool,
      ArrayPool arrayPool,
      ConnectivityMonitorFactory connectivityMonitorFactory,
      int logLevel,
      RequestOptions defaultRequestOptions) {
    this.engine = engine;
    this.bitmapPool = bitmapPool;
    this.arrayPool = arrayPool;
    this.memoryCache = memoryCache;
    this.connectivityMonitorFactory = connectivityMonitorFactory;

    DecodeFormat decodeFormat = defaultRequestOptions.getOptions().get(Downsampler.DECODE_FORMAT);
    bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, decodeFormat);

    final Resources resources = context.getResources();

    Downsampler downsampler =
        new Downsampler(resources.getDisplayMetrics(), bitmapPool, arrayPool);
    ByteBufferGifDecoder byteBufferGifDecoder =
        new ByteBufferGifDecoder(context, bitmapPool, arrayPool);
    registry = new Registry()
        .register(ByteBuffer.class, new ByteBufferEncoder())
        .register(InputStream.class, new StreamEncoder(arrayPool))
        /* Bitmaps */
        .append(ByteBuffer.class, Bitmap.class,
            new ByteBufferBitmapDecoder(downsampler))
        .append(InputStream.class, Bitmap.class,
            new StreamBitmapDecoder(downsampler, arrayPool))
        .append(ParcelFileDescriptor.class, Bitmap.class, new VideoBitmapDecoder(bitmapPool))
        .register(Bitmap.class, new BitmapEncoder())
        /* GlideBitmapDrawables */
        .append(ByteBuffer.class, BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, bitmapPool,
                new ByteBufferBitmapDecoder(downsampler)))
        .append(InputStream.class, BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, bitmapPool,
                new StreamBitmapDecoder(downsampler, arrayPool)))
        .append(ParcelFileDescriptor.class, BitmapDrawable.class,
            new BitmapDrawableDecoder<>(resources, bitmapPool, new VideoBitmapDecoder(bitmapPool)))
        .register(BitmapDrawable.class, new BitmapDrawableEncoder(bitmapPool, new BitmapEncoder()))
        /* Gifs */
        .prepend(InputStream.class, GifDrawable.class,
            new StreamGifDecoder(byteBufferGifDecoder, arrayPool))
        .prepend(ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder)
        .register(GifDrawable.class, new GifDrawableEncoder())
        /* Gif Frames */
        .append(GifDecoder.class, GifDecoder.class, new UnitModelLoader.Factory<GifDecoder>())
        .append(GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool))
        /* Files */
        .register(new ByteBufferRewinder.Factory())
        .append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory())
        .append(File.class, InputStream.class, new FileLoader.StreamFactory())
        .append(File.class, File.class, new FileDecoder())
        .append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory())
        .append(File.class, File.class, new UnitModelLoader.Factory<File>())
        /* Models */
        .register(new InputStreamRewinder.Factory(arrayPool))
        .append(int.class, InputStream.class, new ResourceLoader.StreamFactory(resources))
        .append(
                int.class,
                ParcelFileDescriptor.class,
                new ResourceLoader.FileDescriptorFactory(resources))
        .append(Integer.class, InputStream.class, new ResourceLoader.StreamFactory(resources))
        .append(
                Integer.class,
                ParcelFileDescriptor.class,
                new ResourceLoader.FileDescriptorFactory(resources))
        .append(String.class, InputStream.class, new DataUrlLoader.StreamFactory())
        .append(String.class, InputStream.class, new StringLoader.StreamFactory())
        .append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
        .append(Uri.class, InputStream.class, new HttpUriLoader.Factory())
        .append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()))
        .append(
                Uri.class,
                ParcelFileDescriptor.class,
                new AssetUriLoader.FileDescriptorFactory(context.getAssets()))
        .append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context))
        .append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context))
        .append(
            Uri.class,
             InputStream.class,
             new UriLoader.StreamFactory(context.getContentResolver()))
        .append(Uri.class, ParcelFileDescriptor.class,
             new UriLoader.FileDescriptorFactory(context.getContentResolver()))
        .append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory())
        .append(URL.class, InputStream.class, new UrlLoader.StreamFactory())
        .append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context))
        .append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory())
        .append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory())
        .append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory())
        /* Transcoders */
        .register(Bitmap.class, BitmapDrawable.class,
            new BitmapDrawableTranscoder(resources, bitmapPool))
        .register(Bitmap.class, byte[].class, new BitmapBytesTranscoder())
        .register(GifDrawable.class, byte[].class, new GifDrawableBytesTranscoder());

    ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
    glideContext = new GlideContext(context, registry, imageViewTargetFactory,
        defaultRequestOptions, engine, this, logLevel);
  }

  /**
   * Returns the {@link com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool} used to
   * temporarily store {@link Bitmap}s so they can be reused to avoid garbage
   * collections.
   *
   * <p> Note - Using this pool directly can lead to undefined behavior and strange drawing errors.
   * Any {@link Bitmap} added to the pool must not be currently in use in any other
   * part of the application. Any {@link Bitmap} added to the pool must be removed
   * from the pool before it is added a second time. </p>
   *
   * <p> Note - To make effective use of the pool, any {@link Bitmap} removed from
   * the pool must eventually be re-added. Otherwise the pool will eventually empty and will not
   * serve any useful purpose. </p>
   *
   * <p> The primary reason this object is exposed is for use in custom
   * {@link com.int403.jabong.gifviewer3.glide.load.ResourceDecoder}s and
   * {@link com.int403.jabong.gifviewer3.glide.load.Transformation}s. Use outside of these classes is not generally
   * recommended. </p>
   */
  public BitmapPool getBitmapPool() {
    return bitmapPool;
  }

  public ArrayPool getArrayPool() {
    return arrayPool;
  }

  /**
   * @return The context associated with this instance.
   */
  public Context getContext() {
    return glideContext.getBaseContext();
  }

  ConnectivityMonitorFactory getConnectivityMonitorFactory() {
    return connectivityMonitorFactory;
  }

  GlideContext getGlideContext() {
    return glideContext;
  }

  /**
   * Pre-fills the {@link com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool} using the given
   * sizes.
   *
   * <p> Enough Bitmaps are added to completely fill the pool, so most or all of the Bitmaps
   * currently in the pool will be evicted. Bitmaps are allocated according to the weights of the
   * given sizes, where each size gets (weight / prefillWeightSum) percent of the pool to fill.
   * </p>
   *
   * <p> Note - Pre-filling is done asynchronously using and
   * {@link android.os.MessageQueue.IdleHandler}. Any currently running pre-fill will be cancelled
   * and replaced by a call to this method. </p>
   *
   * <p> This method should be used with caution, overly aggressive pre-filling is substantially
   * worse than not pre-filling at all. Pre-filling should only be started in onCreate to avoid
   * constantly clearing and re-filling the
   * {@link com.int403.jabong.gifviewer3.glide.load.engine.bitmap_recycle.BitmapPool}. Rotation should be carefully
   * considered as well. It may be worth calling this method only when no saved instance state
   * exists so that pre-filling only happens when the Activity is first created, rather than on
   * every rotation. </p>
   *
   * @param bitmapAttributeBuilders The list of
   * {@link com.int403.jabong.gifviewer3.glide.load.engine.prefill.PreFillType.Builder Builders} representing
   * individual sizes and configurations of {@link Bitmap}s to be pre-filled.
   */
  public void preFillBitmapPool(PreFillType.Builder... bitmapAttributeBuilders) {
    bitmapPreFiller.preFill(bitmapAttributeBuilders);
  }

  /**
   * Clears as much memory as possible.
   *
   * @see android.content.ComponentCallbacks#onLowMemory()
   * @see ComponentCallbacks2#onLowMemory()
   */
  public void clearMemory() {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be cleared before bitmap pool to clear re-pooled Bitmaps too. See #687.
    memoryCache.clearMemory();
    bitmapPool.clearMemory();
    arrayPool.clearMemory();
  }

  /**
   * Clears some memory with the exact amount depending on the given level.
   *
   * @see ComponentCallbacks2#onTrimMemory(int)
   */
  public void trimMemory(int level) {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
    memoryCache.trimMemory(level);
    bitmapPool.trimMemory(level);
    arrayPool.trimMemory(level);
  }

  /**
   * Clears disk cache.
   *
   * <p>
   *     This method should always be called on a background thread, since it is a blocking call.
   * </p>
   */
  public void clearDiskCache() {
    Util.assertBackgroundThread();
    engine.clearDiskCache();
  }

  /**
   * Adjusts Glide's current and maximum memory usage based on the given {@link MemoryCategory}.
   *
   * <p> The default {@link MemoryCategory} is {@link MemoryCategory#NORMAL}.
   * {@link MemoryCategory#HIGH} increases Glide's maximum memory usage by up to 50% and
   * {@link MemoryCategory#LOW} decreases Glide's maximum memory usage by 50%. This method should be
   * used to temporarily increase or decrease memory usage for a single Activity or part of the app.
   * Use {@link GlideBuilder#setMemoryCache(MemoryCache)} to put a permanent memory size if you want
   * to change the default. </p>
   */
  public void setMemoryCategory(MemoryCategory memoryCategory) {
    // Engine asserts this anyway when removing resources, fail faster and consistently
    Util.assertMainThread();
    // memory cache needs to be trimmed before bitmap pool to trim re-pooled Bitmaps too. See #687.
    memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
    bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
  }

  /**
   * Begin a load with Glide by passing in a context.
   *
   * <p> Any requests started using a context will only have the application level options applied
   * and will not be started or stopped based on lifecycle events. In general, loads should be
   * started at the level the result will be used in. If the resource will be used in a view in a
   * child fragment, the load should be started with .. using that
   * child fragment. Similarly, if the resource will be used in a view in the parent fragment, the
   * load should be started with ,, using the parent fragment. In
   * the same vein, if the resource will be used in a view in an activity, the load should be
   * started with }. </p>
   *
   * <p> This method is appropriate for resources that will be used outside of the normal fragment
   * or activity lifecycle (For example in services, or for notification thumbnails). </p>
   *
   * @param context Any context, will not be retained.
   * @return A RequestManager for the top level application that can be used to start a load.

   */
  public static RequestManager with(AppCompatActivity context) {
    RequestManagerRetriever retriever = RequestManagerRetriever.get();
    return retriever.get(context);
  }

  public static RequestManager with(Context context) {
    RequestManagerRetriever retriever = RequestManagerRetriever.get();
    return retriever.get(context);
  }

  void removeFromManagers(Target<?> target) {
    synchronized (managers) {
      for (RequestManager requestManager : managers) {
        if (requestManager.untrack(target)) {
          return;
        }
      }
    }
    throw new IllegalStateException("Failed to remove target from managers");
  }

  void registerRequestManager(RequestManager requestManager) {
    synchronized (managers) {
      if (managers.contains(requestManager)) {
        throw new IllegalStateException("Cannot register already registered manager");
      }
      managers.add(requestManager);
    }
  }

  void unregisterRequestManager(RequestManager requestManager) {
    synchronized (managers) {
      if (!managers.contains(requestManager)) {
        throw new IllegalStateException("Cannot register not yet registered manager");
      }
      managers.remove(requestManager);
    }
  }

  @Override
  public void onTrimMemory(int level) {
    trimMemory(level);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    // Do nothing.
  }

  @Override
  public void onLowMemory() {
    clearMemory();
  }
}
