package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByName(String school);

    School findByDirector_Id(Long id);
}
