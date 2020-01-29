package controller;

import model.hibernate.entity.NormalizedWeatherCondition;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Temperature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GroupStats {
    private final int normalizedWeatherConditionNo;
    private final Map<Temperature, Double> temperaturePercentMap = new HashMap<>();
    private final Map<AirHumidity, Double> airHumidityPercentMap = new HashMap<>();
    private final Map<AirPressure, Double> airPressurePercentMap = new HashMap<>();


    public GroupStats(List<NormalizedWeatherCondition> normalizedWeatherConditions) {
        normalizedWeatherConditionNo = normalizedWeatherConditions.size();
        calculateFieldStats(normalizedWeatherConditions, temperaturePercentMap, NormalizedWeatherCondition::getTemperature, Temperature.values());
        calculateFieldStats(normalizedWeatherConditions, airHumidityPercentMap, NormalizedWeatherCondition::getAirHumidity, AirHumidity.values());
        calculateFieldStats(normalizedWeatherConditions, airPressurePercentMap, NormalizedWeatherCondition::getAirPressure, AirPressure.values());
    }

    private <T> void calculateFieldStats(List<NormalizedWeatherCondition> normalizedWeatherConditions, Map<T, Double> percentMap, Function<NormalizedWeatherCondition, T> fieldValue, T[] enumValues) {
        for (T enumValue : enumValues) {
            double partOf = normalizedWeatherConditions.stream()
                    .filter(normalizedWeatherCondition -> fieldValue.apply(normalizedWeatherCondition).equals(enumValue))
                    .count() / (double) normalizedWeatherConditionNo;
            percentMap.put(enumValue, partOf);
        }
    }

    public double calculateProximityFactor(NormalizedWeatherCondition normalizedWeatherCondition) {
        Double temperaturePercent = temperaturePercentMap.get(normalizedWeatherCondition.getTemperature());
        Double airHumidityPercent = airHumidityPercentMap.get(normalizedWeatherCondition.getAirHumidity());
        Double airPressurePercent = airPressurePercentMap.get(normalizedWeatherCondition.getAirPressure());
        return temperaturePercent * airHumidityPercent * airPressurePercent;
    }

    @Override
    public String toString() {
        return "GroupStats{" +
                "normalizedWeatherConditionNo=" + normalizedWeatherConditionNo +
                ", temperaturePercentMap=" + temperaturePercentMap +
                ", airHumidityPercentMap=" + airHumidityPercentMap +
                ", airPressurePercentMap=" + airPressurePercentMap +
                '}';
    }
}
