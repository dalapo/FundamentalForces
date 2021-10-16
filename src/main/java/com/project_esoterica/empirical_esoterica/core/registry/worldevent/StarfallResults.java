package com.project_esoterica.empirical_esoterica.core.registry.worldevent;

import com.project_esoterica.empirical_esoterica.common.worldevent.starfall.StarfallResult;
import com.project_esoterica.empirical_esoterica.common.worldevent.starfall.results.AsteroidStarfallResult;
import com.project_esoterica.empirical_esoterica.common.worldevent.starfall.results.InitialStarDebrisStarfallResult;
import com.project_esoterica.empirical_esoterica.common.worldevent.starfall.results.SpaceDebrisStarfallResult;

import java.util.ArrayList;
import java.util.HashMap;

public class StarfallResults {
    public static HashMap<String, StarfallResult> STARFALL_RESULTS = new HashMap<>();

    public static final AsteroidStarfallResult ASTEROID = new AsteroidStarfallResult();
    public static final SpaceDebrisStarfallResult SPACE_DEBRIS = new SpaceDebrisStarfallResult();
    public static final InitialStarDebrisStarfallResult INITIAL_SPACE_DEBRIS = new InitialStarDebrisStarfallResult();
}
