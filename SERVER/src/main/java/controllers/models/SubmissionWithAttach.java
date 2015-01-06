package controllers.models;

import models.Submission;

public class SubmissionWithAttach extends Submission {
    public String[] previous;
    public String[] attachments;

    public SubmissionWithAttach() {
    }

    public SubmissionWithAttach(Submission submission) {
        this.sid = submission.sid;
        this.uid = submission.uid;
        this.aid = submission.aid;
        this.description = submission.description;
        this.create_date = submission.create_date;
    }
}