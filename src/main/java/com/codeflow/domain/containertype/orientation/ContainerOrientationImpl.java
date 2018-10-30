package com.codeflow.domain.containertype.orientation;

import com.codeflow.domain.algorithm.airforce.layer.Layer;
import com.codeflow.domain.articletype.orientation.ArticleOrientation;
import com.codeflow.domain.containertype.ContainerType;
import com.codeflow.domain.orientation.Orientation;
import com.codeflow.domain.orientation.OrientationImpl;
import com.codeflow.domain.translator.Translator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ContainerOrientationImpl implements ContainerOrientation {

    private Orientation orientation;
    private Double packedVolume;
    private Double packedHeight;
    private Double remainHeight;
    private Double remainLength;
    private ContainerType boxType;
    private Translator translator;

    public ContainerOrientationImpl(Double width, Double height, Double length, ContainerType boxType, Translator translator) {
        this(new OrientationImpl(width, height, length));
        this.boxType = boxType;
        this.translator = translator;
    }

    public ContainerOrientationImpl(Orientation orientation) {
        this.orientation = orientation;
        this.packedVolume = 0D;
        this.packedHeight = 0D;
        this.remainHeight = orientation.getHeight();
        this.remainLength = orientation.getLength();
    }

    public Translator getTranslator() {
        return translator;
    }

    @Override
    public Double getWidth() {
        return orientation.getWidth();
    }

    @Override
    public Double getLength() {
        return orientation.getLength();
    }

    @Override
    public Double getHeight() {
        return orientation.getHeight();
    }

    @Override
    public Double getVolume() {
        return orientation.getVolume();
    }

    @Override
    public boolean fit(Orientation orientation) {
        return this.orientation.fit(orientation);
    }

    @Override
    public Double getPackedVolume() {
        return packedVolume;
    }

    @Override
    public Double getPackedHeight() {
        return packedHeight;
    }

    @Override
    public Double getRemainHeight() {
        return remainHeight;
    }

    @Override
    public Double getRemainLength() {
        return remainLength;
    }

    public void pack(ArticleOrientation articleOrientation) {
        packedVolume = packedVolume + articleOrientation.getVolume();
    }

    public void pack(Layer layer) {
        packedHeight = packedHeight + layer.getHeight();
        remainHeight = getHeight() - packedHeight;
    }

    @Override
    public boolean allVolumePacked() {
        return (getVolume() - packedVolume) == 0D;
    }

    @Override
    public ContainerType getBoxType() {
        return boxType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContainerOrientationImpl that = (ContainerOrientationImpl) o;

        return new EqualsBuilder()
                .append(orientation, that.orientation)
                .append(packedVolume, that.packedVolume)
                .append(packedHeight, that.packedHeight)
                .append(remainHeight, that.remainHeight)
                .append(remainLength, that.remainLength)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orientation)
                .append(packedVolume)
                .append(packedHeight)
                .append(remainHeight)
                .append(remainLength)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ContainerOrientationImpl{" +
                "orientation=" + orientation +
                ", packedVolume=" + packedVolume +
                ", packedHeight=" + packedHeight +
                ", remainHeight=" + remainHeight +
                ", remainLength=" + remainLength +
                '}';
    }
}
