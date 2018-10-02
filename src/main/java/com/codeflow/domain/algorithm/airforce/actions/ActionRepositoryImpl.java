package com.codeflow.domain.algorithm.airforce.actions;

import com.codeflow.domain.algorithm.airforce.packing.PackingService;
import com.codeflow.domain.algorithm.airforce.searching.SearchingService;
import com.codeflow.domain.algorithm.airforce.topology.situations.Situation;

import java.util.HashMap;
import java.util.Map;

class ActionRepositoryImpl implements ActionRepository<Action> {

    private Map<Situation, Action> situationRunnableMap;

    ActionRepositoryImpl(SearchingService searchingService,
                         PackingService packingService) {
        situationRunnableMap = new HashMap<>();
        situationRunnableMap.put(Situation.NO_BOXES_ON_THE_RIGHT_AND_LEFT_SIDES, new NoBoxesOnTheRightAndLeftSidesAction(searchingService,

                packingService));
        situationRunnableMap.put(Situation.NO_BOXES_ON_THE_LEFT_SIDE, new NoBoxesOnTheLeftAction(searchingService,

                packingService));
        situationRunnableMap.put(Situation.NO_BOXES_ON_THE_RIGHT_SIDE, new NoBoxesOnTheRightAction(searchingService,

                packingService));
        situationRunnableMap.put(Situation.THERE_ARE_BOXES_ON_BOTH_SIDES_AND_EQUAL, new ThereAreBoxesOnBothSidesAndEqualAction());
        situationRunnableMap.put(Situation.THERE_ARE_BOXES_ON_BOTH_SIDES_AND_NOT_EQUAL, new ThereAreBoxesOnBothSidesAndNotEqualAction());
    }

    @Override
    public Action findBy(Situation topologySituation) {
        return situationRunnableMap.get(topologySituation);
    }
}
