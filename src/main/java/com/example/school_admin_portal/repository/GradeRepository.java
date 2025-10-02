package com.example.school_admin_portal.repository;

import com.example.school_admin_portal.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {
    List<GradeEntity> findByStudentId(Long studentId);

    List<GradeEntity> findByStudentIdAndSemester(Long studentId, String semester);

    List<GradeEntity> findByStudentIdAndAcademicYear(Long studentId, String academicYear);

    @Query("SELECT AVG(g.points) FROM GradeEntity g WHERE g.studentId = ?1")
    Double calculateGPAByStudentId(Long studentId);

    @Query("SELECT SUM(g.credits) FROM GradeEntity g WHERE g.studentId = ?1 AND g.status = 'Passed'")
    Integer getTotalCreditsEarnedByStudentId(Long studentId);
}
