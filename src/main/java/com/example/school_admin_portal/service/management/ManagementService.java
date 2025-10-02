package com.example.school_admin_portal.service.management;

import com.example.school_admin_portal.entity.SchoolEntity;
import com.example.school_admin_portal.repository.management.SchoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService implements IManagementService {

    private final SchoolRepository schoolRepository;

    public ManagementService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<SchoolEntity> findAllSchools() {
        return schoolRepository.findAll();
    }

    @Override
    public SchoolEntity findSchoolById(Long id) {
        return schoolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("School not found with id: " + id));
    }

    @Override
    public void saveSchool(SchoolEntity school) {
        schoolRepository.save(school);
    }

    @Override
    public SchoolEntity updateSchool(Long id, SchoolEntity school) {
        SchoolEntity existing = schoolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("School not found with id: " + id));
        existing.setName(school.getName());
        existing.setAddress(school.getAddress());
        existing.setPhone(school.getPhone());
        existing.setEmail(school.getEmail());
        existing.setWebsite(school.getWebsite());
        existing.setAffiliation(school.getAffiliation());
        existing.setEstablishedYear(school.getEstablishedYear());
        existing.setMediumOfInstruction(school.getMediumOfInstruction());
        existing.setNumberOfStudents(school.getNumberOfStudents());
        existing.setNumberOfTeachers(school.getNumberOfTeachers());
        existing.setPrincipalName(school.getPrincipalName());
        existing.setState(school.getState());
        existing.setCountry(school.getCountry());
        existing.setPostalCode(school.getPostalCode());
        schoolRepository.save(existing);
        return existing;
    }

    @Override
    public void deleteSchoolById(Long id) {
        schoolRepository.deleteById(id);
    }
}
