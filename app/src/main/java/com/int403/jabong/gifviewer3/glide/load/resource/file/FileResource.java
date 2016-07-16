package com.int403.jabong.gifviewer3.glide.load.resource.file;

import com.int403.jabong.gifviewer3.glide.load.resource.SimpleResource;

import java.io.File;

/**
 * A simple {@link com.int403.jabong.gifviewer3.glide.load.engine.Resource} that wraps a {@link File}.
 */
public class FileResource extends SimpleResource<File> {
  public FileResource(File file) {
    super(file);
  }
}
