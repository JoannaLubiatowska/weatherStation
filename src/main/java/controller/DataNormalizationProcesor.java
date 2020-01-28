package controller;

import model.entity.NormalizedWeatherCondition;
import model.entity.WeatherCondition;

import java.util.*;
import java.util.stream.Collectors;

public class DataNormalizationProcesor {

    public static List<NormalizedWeatherCondition> normalizeData(List<WeatherCondition> weatherCondition) {
        return new ArrayList<>();
    }

    public static ClassificationProblem getClassification(List<WeatherCondition> weatherCondition) {
        Map<Boolean, List<NormalizedWeatherCondition>> recordsMap = normalizeData(weatherCondition)
                .stream()
                .collect(Collectors.groupingBy(normalizedWeatherCondition -> Objects.isNull(normalizedWeatherCondition.getInferenceResult())));
        return new ClassificationProblem(new HashSet<>(recordsMap.get(false)), new HashSet<>(recordsMap.get(true)));
    }
}
