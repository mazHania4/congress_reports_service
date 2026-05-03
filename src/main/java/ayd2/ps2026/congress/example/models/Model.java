package ayd2.ps2026.congress.example.models;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

//@Entity
//@DynamicUpdate
@NoArgsConstructor
@Data
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class Model {
        //extends Auditor {

    /*
    // for enums
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdTypeEnum type;
    */

    //@Column(nullable = false)
    private Float costPerHour;

    /*
    //relations
    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser appUser;
    */

}
