package com.example.school_admin_portal.service.management;

import com.example.school_admin_portal.entity.SchoolEntity;

import java.util.List;

public interface IManagementService {

    List<SchoolEntity> findAllSchools();

    SchoolEntity findSchoolById(Long id);

    void saveSchool(SchoolEntity school);

    SchoolEntity updateSchool(Long id, SchoolEntity school);

    void deleteSchoolById(Long id);


}
