package com.int403.jabong.gifviewer3.glide.manager;

import com.int403.jabong.gifviewer3.glide.RequestManager;

import java.util.Collections;
import java.util.Set;

/**
 * A {@link RequestManagerTreeNode} that returns no relatives.
 */
final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
    @Override
    public Set<RequestManager> getDescendants() {
        return Collections.emptySet();
    }
}
