package org.ahmet.sbpostgdocrest.participant;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "participants", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Role cannot be empty")
    @Pattern(regexp = "BA|Dev|DevOps|SDET|ScrumMaster|PO", message = "Role must be one of BA, Dev, DevOps, SDET, ScrumMaster, PO")
    private String role;

    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

}
