package com.codeflow.domain.orientation;

import com.codeflow.domain.dimension.Dimensions;

public interface Orientation {
    Dimensions getDimensions();

    Double getWidth();

    Double getLength();

    Double getHeight();

    boolean fit(Orientation orientation);

}