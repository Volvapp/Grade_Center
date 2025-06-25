package com.uni.GradeCenter.model.dto;

public class ParentDTO {
    private Long id;
    private Long userId;
    private Long childId;

    public ParentDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }
}
