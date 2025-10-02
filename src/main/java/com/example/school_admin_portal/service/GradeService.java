package com.example.school_admin_portal.service;

import com.example.school_admin_portal.entity.GradeEntity;
import com.example.school_admin_portal.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<GradeEntity> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<GradeEntity> getGradesByStudentIdAndSemester(Long studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }

    public Double calculateGPA(Long studentId) {
        Double gpa = gradeRepository.calculateGPAByStudentId(studentId);
        return gpa != null ? Math.round(gpa * 100.0) / 100.0 : 0.0;
    }

    public Integer getTotalCreditsEarned(Long studentId) {
        Integer credits = gradeRepository.getTotalCreditsEarnedByStudentId(studentId);
        return credits != null ? credits : 0;
    }

    public GradeEntity saveGrade(GradeEntity grade) {
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public List<String> getAvailableSemesters(Long studentId) {
        return gradeRepository.findByStudentId(studentId)
                .stream()
                .map(GradeEntity::getSemester)
                .distinct()
                .sorted()
                .toList();
    }
}
