package com.codeflow.domain.translator;

import com.codeflow.domain.articletype.orientation.ArticleOrientation;
import com.codeflow.domain.articletype.orientation.ArticleOrientationImpl;
import com.codeflow.domain.position.Position;
import com.codeflow.domain.position.PositionImpl;

public class LHWTranslator implements Translator {
    @Override
    public Position translate(Position position) {
        return new PositionImpl(position.getZ(), position.getY(), position.getX());
    }

    @Override
    public ArticleOrientation translate(ArticleOrientation orientation) {
        return new ArticleOrientationImpl(orientation.getLength(), orientation.getHeight(), orientation.getWidth(), orientation.getBoxType());
    }
}
