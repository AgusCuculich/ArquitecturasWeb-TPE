package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Long mobile;
}
