package com.codeflow.domain.articletype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleRepositoryImpl implements ArticleTypeRepository {

    private Map<ArticleType, Long> receivedArticleTypes;
    private Map<ArticleType, Long> remainingToPack;

    public ArticleRepositoryImpl() {
        this.receivedArticleTypes = new HashMap<>();
        this.remainingToPack = new HashMap<>();
    }

    @Override
    public void saveType(ArticleType a, Long number) {
        this.receivedArticleTypes.put(a, number);
        this.remainingToPack.put(a, number);
    }

    @Override
    public Map<ArticleType, Long> receivedArticleTypes() {
        return new HashMap<>(receivedArticleTypes);
    }

    @Override
    public List<ArticleType> unpackedTypes() {
        return remainingToPack.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public void savePack(ArticleType articleType) {
        Long sizeToPack = this.remainingToPack.get(articleType);
        if (sizeToPack > 0) {
            this.remainingToPack.put(articleType, sizeToPack - 1);
        } else {
            throw new IllegalStateException("Cannot pack");
        }
    }

    @Override
    public boolean areAllPacked() {
        return remainingToPack.entrySet().stream().allMatch(e -> e.getValue() == 0);
    }

    @Override
    public void reset() {
        this.remainingToPack = new HashMap<>(receivedArticleTypes);
    }

    public void clear() {
        receivedArticleTypes.clear();
        remainingToPack.clear();
    }

}