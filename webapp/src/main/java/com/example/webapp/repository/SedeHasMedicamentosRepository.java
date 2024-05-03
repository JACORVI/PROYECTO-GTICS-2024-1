package com.example.webapp.repository;

import com.example.webapp.entity.SedeHasMedicamentos;
import com.example.webapp.entity.SedeHasMedicamentosId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SedeHasMedicamentosRepository extends JpaRepository<SedeHasMedicamentos, SedeHasMedicamentosId> {

}
