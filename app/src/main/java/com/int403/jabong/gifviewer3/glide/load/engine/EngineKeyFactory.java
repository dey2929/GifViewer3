package com.int403.jabong.gifviewer3.glide.load.engine;

import com.int403.jabong.gifviewer3.glide.load.Key;
import com.int403.jabong.gifviewer3.glide.load.Options;
import com.int403.jabong.gifviewer3.glide.load.Transformation;

import java.util.Map;

class EngineKeyFactory {

  @SuppressWarnings("rawtypes")
  public EngineKey buildKey(Object model, Key signature, int width, int height,
      Map<Class<?>, Transformation<?>> transformations, Class<?> resourceClass,
      Class<?> transcodeClass, Options options) {
    return new EngineKey(model, signature, width, height, transformations, resourceClass,
        transcodeClass, options);
  }
}
