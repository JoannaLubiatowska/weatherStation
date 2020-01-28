package model.entity;

import lombok.Builder;
import lombok.Data;
import model.entity.InferenceResult;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Temperature;

import java.sql.Timestamp;

@Data
@Builder
public class NormalizedWeatherCondition {
    private Timestamp measurementTime;
    private Temperature temperature;
    private AirHumidity airHumidity;
    private AirPressure airPressure;
    private InferenceResult inferenceResult;
}