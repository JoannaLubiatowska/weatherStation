package controller;

import model.hibernate.entity.NormalizedWeatherCondition;
import model.enums.Inference;

import java.util.*;
import java.util.stream.Collectors;

public class BayesClassifier {

    private Map<Inference, GroupStats> groupStatsMap = new HashMap<>();

    void configure(Collection<NormalizedWeatherCondition> trainingSet) {
        trainingSet.stream()
                .collect(Collectors.groupingBy(NormalizedWeatherCondition::getInference))
                .forEach(this::addGroupStats);
    }

    private void addGroupStats(Inference inference, List<NormalizedWeatherCondition> normalizedWeatherConditions) {
        groupStatsMap.put(inference, new GroupStats(normalizedWeatherConditions));
    }

    void classify(NormalizedWeatherCondition normalizedWeatherCondition) {
        Inference group = groupStatsMap.entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().calculateProximityFactor(normalizedWeatherCondition)))
                .max(Comparator.comparingDouble(Map.Entry::getValue)).orElseThrow().getKey();
        normalizedWeatherCondition.setInference(group);
    }

    @Override
    public String toString() {
        return "BayesClassifier{" +
                "groupStatsMap=\n" + groupStatsMap +
                "}";
    }
}
