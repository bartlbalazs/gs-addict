package hu.bartl.gsAddict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ad {

    @Id
    private String url;
    private String name;
    private String image;
    private String price;
    private LocalDateTime createdAt = LocalDateTime.now();
}
