package model.entity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "InferenceResults", uniqueConstraints = { @UniqueConstraint(columnNames = { "inferenceResultsID" }) })
public class InferenceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inferenceResultsID;
    private String inferenceResultsName;
    private String inferenceResultsDescription;
    @ToString.Exclude
    @OneToMany(mappedBy = "inferenceResult", cascade = CascadeType.PERSIST)
    private final List<WeatherCondition> weatherConditionsList = new ArrayList<>();
}
