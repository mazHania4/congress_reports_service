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
@Table(name = "registrations", schema = "registration_ext_schema")
public class Registration {

    @Id
    private Long registrationId;

    @Column
    private Long userId;

    @Column
    private Long congressId;

    @Column
    private LocalDateTime registrationDate;

}