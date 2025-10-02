package com.example.school_admin_portal.repository.student;

import com.example.school_admin_portal.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> findByNameOrderByNameAsc(String name);


    List<StudentEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(String name, String email, String course);

    List<StudentEntity> findByFeeBetween(Double feeAfter, Double feeBefore);
}
