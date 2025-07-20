package com.example.monproject.repository;

import com.example.monproject.model.Donneur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonneurRepository extends JpaRepository<Donneur, Long> {

    Optional<Donneur> findByEmail(String email);
    long count();
    long countBySexe(String sexe);
    long countByGroupeSanguin(String groupe); // Si groupe stocké comme "A+", "O-", etc.

    @Query(
            value = "SELECT COUNT(*) FROM donneur d WHERE EXTRACT(YEAR FROM AGE(CURRENT_DATE, d.date_naissance)) BETWEEN :min AND :max",
            nativeQuery = true
    )
    int countByAgeBetween(@Param("min") int min, @Param("max") int max);


    @Query(value = "SELECT COUNT(*) FROM donneur d WHERE d.date_naissance <= (CURRENT_DATE - make_interval(years => :age))", nativeQuery = true)
    int countByAgeGreaterThanEqual(@Param("age") int age);


}

