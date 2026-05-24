package ayd2.ps2026.congress.models.registration;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "workshop_reservations", schema = "registration_ext_schema")
public class WorkshopReservation {

    @Id
    private Long reservationId;

    @Column
    private Long userId;

    @Column
    private Long activityId;

    @Column
    private LocalDateTime reservationDate;

}