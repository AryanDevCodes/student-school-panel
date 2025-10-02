package com.example.school_admin_portal.service.student;


import com.example.school_admin_portal.entity.StudentEntity;

import java.util.List;

public interface IStudentService {
    List<StudentEntity> getAllStudents();

    StudentEntity getStudentById(Long id);

    StudentEntity createStudent(StudentEntity student);

    StudentEntity updateStudent(Long id, StudentEntity student);

    void deleteStudent(Long id);

    List<StudentEntity> getStudentsByName(String name);

    List<StudentEntity> findByFeeBetween(Double feeAfter, Double feeBefore);

    List<StudentEntity> searchStudents(String keyword);
}
