package com.int403.jabong.gifviewer3.glide.load.engine.cache;

import android.support.v4.util.Pools;

import com.int403.jabong.gifviewer3.glide.load.Key;
import com.int403.jabong.gifviewer3.glide.util.LruCache;
import com.int403.jabong.gifviewer3.glide.util.Util;
import com.int403.jabong.gifviewer3.glide.util.pool.FactoryPools;
import com.int403.jabong.gifviewer3.glide.util.pool.StateVerifier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class that generates and caches safe and unique string file names from {@link
 * com.int403.jabong.gifviewer3.glide.load.Key}s.
 */
public class SafeKeyGenerator {
  private final LruCache<Key, String> loadIdToSafeHash = new LruCache<>(1000);
  private final Pools.Pool<PoolableDigestContainer> digestPool = FactoryPools.threadSafe(10,
      new FactoryPools.Factory<PoolableDigestContainer>() {
        @Override
        public PoolableDigestContainer create() {
          try {
            return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
          } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
          }
        }
      });

  public String getSafeKey(Key key) {
    String safeKey;
    synchronized (loadIdToSafeHash) {
      safeKey = loadIdToSafeHash.get(key);
    }
    if (safeKey == null) {
      safeKey = calculateHexStringDigest(key);
    }
    synchronized (loadIdToSafeHash) {
      loadIdToSafeHash.put(key, safeKey);
    }
    return safeKey;
  }

  private String calculateHexStringDigest(Key key) {
    PoolableDigestContainer container = digestPool.acquire();
    try {
      key.updateDiskCacheKey(container.messageDigest);
      // calling digest() will automatically reset()
      return Util.sha256BytesToHex(container.messageDigest.digest());
    } finally {
      digestPool.release(container);
    }
  }

  private static final class PoolableDigestContainer implements FactoryPools.Poolable {

    private final MessageDigest messageDigest;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();

    PoolableDigestContainer(MessageDigest messageDigest) {
      this.messageDigest = messageDigest;
    }

    @Override
    public StateVerifier getVerifier() {
      return stateVerifier;
    }
  }
}
