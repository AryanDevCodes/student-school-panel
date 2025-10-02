package com.example.school_admin_portal.repository;

import com.example.school_admin_portal.entity.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, Long> {
    List<FeeEntity> findByStudentId(Long studentId);

    List<FeeEntity> findByStudentIdAndSemester(Long studentId, String semester);

    List<FeeEntity> findByStudentIdAndAcademicYear(Long studentId, String academicYear);

    List<FeeEntity> findByStudentIdAndStatus(Long studentId, String status);

    @Query("SELECT SUM(f.amount) FROM FeeEntity f WHERE f.studentId = ?1")
    Double getTotalFeeByStudentId(Long studentId);

    @Query("SELECT SUM(f.amount) FROM FeeEntity f WHERE f.studentId = ?1 AND f.status = 'Paid'")
    Double getPaidFeeByStudentId(Long studentId);

    @Query("SELECT SUM(f.amount) FROM FeeEntity f WHERE f.studentId = ?1 AND f.status = 'Pending'")
    Double getPendingFeeByStudentId(Long studentId);
}
