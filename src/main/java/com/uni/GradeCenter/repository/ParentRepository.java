package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    void deleteByUser_Id(Long id);

    Parent findByUser_Id(Long id);
}
