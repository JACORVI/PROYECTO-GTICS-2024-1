package com.example.webapp.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class SedeHasMedicamentosId implements Serializable {

    @Column(name = "sede_id_sede", nullable = false)
    private Integer id_sede;
    @Column(name = "medicamentos_id_medicamentos", nullable = false)
    private Integer id_medicamentos;
}
