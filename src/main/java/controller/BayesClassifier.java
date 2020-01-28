package controller;

import model.entity.InferenceResult;
import model.entity.NormalizedWeatherCondition;
import java.util.*;
import java.util.stream.Collectors;

public class BayesClassifier {

    private Map<InferenceResult, GroupStats> groupStatsMap = new HashMap<>();

    void configure(Collection<NormalizedWeatherCondition> trainingSet) {
        trainingSet.stream()
                .collect(Collectors.groupingBy(NormalizedWeatherCondition::getInferenceResult))
                .forEach(this::addGroupStats);
    }

    private void addGroupStats(InferenceResult inferenceResult, List<NormalizedWeatherCondition> normalizedWeatherConditions) {
        groupStatsMap.put(inferenceResult, new GroupStats(normalizedWeatherConditions));
    }

    void classify(NormalizedWeatherCondition normalizedWeatherCondition) {
        InferenceResult group = groupStatsMap.entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().calculateProximityFactor(normalizedWeatherCondition)))
                .max(Comparator.comparingDouble(Map.Entry::getValue)).orElseThrow().getKey();
        normalizedWeatherCondition.setInferenceResult(group);
    }

    @Override
    public String toString() {
        return "BayesClassifier{" +
                "groupStatsMap=\n" + groupStatsMap +
                "}";
    }
}
