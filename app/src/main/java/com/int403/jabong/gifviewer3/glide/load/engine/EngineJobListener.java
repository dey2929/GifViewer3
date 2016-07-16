package com.int403.jabong.gifviewer3.glide.load.engine;

import com.int403.jabong.gifviewer3.glide.load.Key;

interface EngineJobListener {

  void onEngineJobComplete(Key key, EngineResource<?> resource);

  void onEngineJobCancelled(EngineJob engineJob, Key key);
}
