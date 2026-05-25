package ayd2.ps2026.congress.models.auth;

import ayd2.ps2026.congress.common.models.entities.Auditor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * Usuario interno de la aplicación
 * 
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-28
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Immutable
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "app_user", schema = "auth_ext_schema")
public class AppUser extends Auditor {

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String lastname;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private Boolean mfaActive;

    @Column
    private String cellphone;

    @Column
    private String identification;

    @Column
    private String photoUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "app_user_role",
            schema = "auth_ext_schema",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
