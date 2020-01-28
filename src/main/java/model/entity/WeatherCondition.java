package model.entity;

import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;
import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "WeatherCondition", uniqueConstraints = { @UniqueConstraint(columnNames = { "weatherConditionsID" }) })
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int weatherConditionsID;
    private Timestamp measurementTime;
    private double temperature;
    private double airHumidity;
    private double airPressure;
    @ManyToOne
    private InferenceResult inferenceResult;
}
