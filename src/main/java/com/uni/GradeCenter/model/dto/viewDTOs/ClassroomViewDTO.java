package com.uni.GradeCenter.model.dto.viewDTOs;

public class ClassroomViewDTO {
    private String classroomName;
    private Long classroomId;
    private String schoolName;
    private String schoolDirectorName;

    public ClassroomViewDTO(String classroomName, Long classroomId, String schoolName, String schoolDirectorName) {
        this.classroomName = classroomName;
        this.classroomId = classroomId;
        this.schoolName = schoolName;
        this.schoolDirectorName = schoolDirectorName;
    }

    public ClassroomViewDTO() {
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolDirectorName() {
        return schoolDirectorName;
    }

    public void setSchoolDirectorName(String schoolDirectorName) {
        this.schoolDirectorName = schoolDirectorName;
    }
}
