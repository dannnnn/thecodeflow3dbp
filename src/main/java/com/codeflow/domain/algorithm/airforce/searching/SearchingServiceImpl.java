package com.codeflow.domain.algorithm.airforce.searching;

import com.codeflow.domain.articletype.ArticleType;
import com.codeflow.domain.articletype.orientation.ArticleOrientation;
import com.codeflow.domain.boxtype.BoxType;
import com.codeflow.domain.gap.Gap;
import com.codeflow.domain.orientation.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SearchingServiceImpl implements SearchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchingServiceImpl.class);

    private List<ArticleType> boxTypes;

    public SearchingServiceImpl(Map<ArticleType, Long> articles) {
        this.boxTypes = articles.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public SearchingServiceImpl(List<ArticleType> boxTypes) {
        this.boxTypes = boxTypes;
    }

    /**
     * Sort in precedence order, a box with a y-dimension closest to Hy
     * but not more than Hmy, with an x-dimension closest to, but not more than Hmx, and a z dimension closest to Hz,
     * but not more than Hmz. This means it considers the y dimension, then among boxes having the same y-dimension,
     * it looks at the x-dimension, and finally among the boxes having the same y- and x-dimension,
     * it looks at the z dimension. It calculates the differences between the gap's dimensions and the box
     * dimensions for each box and picks the box with the least differences to be the best fitting
     * one. It also finds a second box with a y-dimension bigger than the current layer
     * thickness, but closest to the current layer thickness. Boxes that fit and are the proper
     * thickness (y-values) are packed.
     */

    @Override
    public SearchResult findBoxTypes(Gap requiredGap, Gap maxGap) {
        long start = System.nanoTime();
//        LOGGER.info("FIND_BOX:{},{},{},{},{}", maxGap.getWidth(), maxGap.getHeight(), maxGap.getLength(), requiredGap.getHeight(), requiredGap.getLength());
//        LOGGER.info("Searching box for requiredGap[{}] and maxGap [{}]", requiredGap, maxGap);
        SearchResult searchResult = new SearchResult();
        List<ArticleOrientation> allOrientationsOfAllBoxTypes = boxTypes.stream().map(BoxType::getOrientations).flatMap(Collection::stream).collect(Collectors.toList());

        //Filter orientations that does not fit in maximum gap;
        List<ArticleOrientation> orientationsFitsMaxGap = allOrientationsOfAllBoxTypes.stream().filter(maxGap::fit).collect(Collectors.toList());

        List<ArticleOrientation> smallerThenRequiredGapHeight = orientationsFitsMaxGap.stream().filter(requiredGap::smallerThenHeight).collect(Collectors.toList());
        ArrayList<ArticleOrientation> biggerThenRequiredGapHeight = new ArrayList<>(orientationsFitsMaxGap);
        biggerThenRequiredGapHeight.removeAll(smallerThenRequiredGapHeight);

        if (smallerThenRequiredGapHeight.size() > 0) {
            searchSmallerAndClosestToRequiredHeight(requiredGap, searchResult, smallerThenRequiredGapHeight);
        }
        if (biggerThenRequiredGapHeight.size() > 0) {
            searchBiggerAndClosestToRequiredHeight(requiredGap, searchResult, biggerThenRequiredGapHeight);
        }
//        System.out.println(System.nanoTime() - execute);
        return searchResult;
    }

    private void searchBiggerAndClosestToRequiredHeight(Gap requiredGap, SearchResult searchResult, ArrayList<ArticleOrientation> biggerThenRequiredGapHeight) {
        List<ArticleOrientation> sortedBiggerThenRequiredGapHeight = biggerThenRequiredGapHeight.stream()
                .sorted(Comparator.comparingDouble((Orientation o) -> Math.abs(o.getHeight() - requiredGap.getHeight()))
                        .thenComparingDouble((Orientation o) -> Math.abs(o.getWidth() - requiredGap.getWidth()))
                        .thenComparingDouble((Orientation o) -> Math.abs(requiredGap.getLength() - o.getLength()))).collect(Collectors.toList());
        ArticleOrientation biggerThenRequiredGap = sortedBiggerThenRequiredGapHeight.get(0);
        searchResult.addBestFitBiggerThenRequired(biggerThenRequiredGap);
    }

    private void searchSmallerAndClosestToRequiredHeight(Gap requiredGap, SearchResult searchResult, List<ArticleOrientation> smallerThenRequiredGapHeight) {
        List<ArticleOrientation> sortedSmallerThenRequiredGapHeight = smallerThenRequiredGapHeight.stream()
                .sorted(Comparator.comparingDouble((Orientation o) -> Math.abs(requiredGap.getHeight() - o.getHeight()))
                        .thenComparingDouble((Orientation o) -> Math.abs(requiredGap.getWidth() - o.getWidth()))
                        .thenComparingDouble((Orientation o) -> Math.abs(requiredGap.getLength() - o.getLength())))
                .collect(Collectors.toList());
        ArticleOrientation bestFitInRequiredGap = sortedSmallerThenRequiredGapHeight.get(0);
        searchResult.addBestFitInRequired(bestFitInRequiredGap);
    }
}
