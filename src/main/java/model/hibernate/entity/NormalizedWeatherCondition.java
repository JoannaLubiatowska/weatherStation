package model.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Inference;
import model.enums.Temperature;


@Data
@AllArgsConstructor
@ToString
public class NormalizedWeatherCondition {
    private Integer weatherConditionsID;
    private Temperature temperature;
    private AirHumidity airHumidity;
    private AirPressure airPressure;
    private Inference inference;

    public NormalizedWeatherCondition(Integer weatherConditionsID, Temperature temperature, AirHumidity airHumidity, AirPressure airPressure) {
        this.weatherConditionsID = weatherConditionsID;
        this.temperature = temperature;
        this.airHumidity = airHumidity;
        this.airPressure = airPressure;
    }
}
