package model.entity;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "InferenceResult", uniqueConstraints = { @UniqueConstraint(columnNames = { "inferenceResultsID" }) })
public class InferenceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inferenceResultsID;
    private String inferenceResultsName;
    private String inferenceResultsDescription;
    @OneToMany(mappedBy = "InferenceResult", cascade = CascadeType.PERSIST)
    private final List<WeatherCondition> weatherConditionsList = new ArrayList<>();
}
