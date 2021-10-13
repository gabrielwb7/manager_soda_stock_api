package one.digitalinnovation.sodastock.repository;

import one.digitalinnovation.sodastock.entity.Soda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SodaRepository extends JpaRepository<Soda, Long> {

    Optional<Soda> findByName(String name);

}