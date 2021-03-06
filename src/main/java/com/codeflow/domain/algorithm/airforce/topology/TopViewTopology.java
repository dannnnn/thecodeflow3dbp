package com.codeflow.domain.algorithm.airforce.topology;

import com.codeflow.domain.algorithm.airforce.topology.corner.Corner;

import java.util.List;

public interface TopViewTopology {
    List<Corner> getRightCorners();

    boolean hasCornerOnLeft(Corner corner);

    boolean hasCornerOnRight(Corner corner);

    Corner getRightCorner(Corner corner);

    Corner getLeftCorner(Corner corner);

    void addLast(Corner corner);

    void addFirst(Corner corner);

    void addAfter(Corner target, Corner toAdd);

    void addBefore(Corner target, Corner toAdd);

    Corner findWithSmallestLength();

    void remove(Corner corner);

    void removeAndAllOnRight(Corner smallestZ);
}
