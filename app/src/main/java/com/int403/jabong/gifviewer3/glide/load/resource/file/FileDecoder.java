package com.int403.jabong.gifviewer3.glide.load.resource.file;

import com.int403.jabong.gifviewer3.glide.load.Options;
import com.int403.jabong.gifviewer3.glide.load.ResourceDecoder;
import com.int403.jabong.gifviewer3.glide.load.engine.Resource;

import java.io.File;

/**
 * A simple {@link com.int403.jabong.gifviewer3.glide.load.ResourceDecoder} that creates resource for a given {@link
 * File}.
 */
public class FileDecoder implements ResourceDecoder<File, File> {

  @Override
  public boolean handles(File source, Options options) {
    return true;
  }

  @Override
  public Resource<File> decode(File source, int width, int height, Options options) {
    return new FileResource(source);
  }
}
