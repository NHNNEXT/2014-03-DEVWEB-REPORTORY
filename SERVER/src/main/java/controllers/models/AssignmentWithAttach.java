package controllers.models;

import models.Assignment;

public class AssignmentWithAttach extends Assignment {
    public AssignmentWithAttach() {
    }

    public AssignmentWithAttach(Assignment assignment) {
        this.aid = assignment.aid;
        this.lid = assignment.lid;
        this.createTime = assignment.createTime;
        this.dueTime = assignment.dueTime;
        this.title = assignment.title;
        this.description = assignment.description;
    }

    public String[] attachments;
}