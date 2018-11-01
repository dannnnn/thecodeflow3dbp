package com.codeflow.domain.algorithm.airforce;

import com.codeflow.domain.algorithm.airforce.searching.SearchResult;
import com.codeflow.domain.algorithm.airforce.searching.SearchingService;
import com.codeflow.domain.algorithm.airforce.searching.SearchingServiceImpl;
import com.codeflow.domain.articletype.ArticleType;
import com.codeflow.domain.articletype.orientation.ArticleOrientation;
import com.codeflow.domain.containertype.orientation.ContainerOrientation;
import com.codeflow.domain.gap.Gap;
import com.codeflow.domain.position.Position;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IterationSession {

    private Map<Position, ArticleOrientation> packedTypes;
    private Map<ArticleType, Long> remainingToPack;
    private double packedVolume;
    public boolean hundredPercentPacked;
    private ContainerOrientation containerOrientation;

    public IterationSession(Map<ArticleType, Long> receivedArticleTypes) {
        this.packedTypes = new LinkedHashMap<>();
        this.remainingToPack = new LinkedHashMap<>(receivedArticleTypes);
    }

    public IterationSession(Map<ArticleType, Long> receivedArticleTypes,
                            ContainerOrientation containerOrientation) {
        this.packedTypes = new LinkedHashMap<>();
        this.remainingToPack = new LinkedHashMap<>(receivedArticleTypes);
        this.packedVolume = 0;
        this.containerOrientation = containerOrientation;
        this.hundredPercentPacked = false;
    }



    public List<ArticleType> unpackedTypes() {
        return remainingToPack.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void pack(ArticleOrientation articleOrientation, Position position) {
        ArticleType articleType = articleOrientation.getBoxType();
        Long sizeToPack = this.remainingToPack.get(articleType);
        if (sizeToPack > 0) {
            long newSize = sizeToPack - 1;
            if (newSize > 0) {
                this.remainingToPack.put(articleType, newSize);
            } else {
                this.remainingToPack.remove(articleType);
            }
        } else {
            throw new IllegalStateException("Cannot pack");
        }
        packedTypes.put(position, articleOrientation);
        System.out.println(String.format("PACK: %s %s %s %s %s %s", position.getX(), position.getY(), position.getZ(), articleOrientation.getWidth(), articleOrientation.getHeight(), articleOrientation.getLength()));

    }

    public Map<Position, ArticleOrientation> getPackedTypes() {
        return new LinkedHashMap<>(packedTypes);
    }

    public Map<ArticleType, Long> getRemainingToPack() {
        return remainingToPack;
    }

    public ContainerOrientation getContainerOrientation() {
        return containerOrientation;
    }


    public double getPackedVolume() {
        return packedVolume;
    }

    public void setPackedVolume(double packedVolume) {
        this.packedVolume = packedVolume;
    }

    public SearchResult findBoxTypes(List<ArticleType> articleTypes, Gap requiredGapImpl, Gap maxGapImpl) {
        SearchingService searchingService = new SearchingServiceImpl(articleTypes);
        return searchingService.findBoxTypes(requiredGapImpl, maxGapImpl);
    }
}
