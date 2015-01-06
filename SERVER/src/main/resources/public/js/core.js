/*!
 * Core library for REPORTORY
 * It needs jQuery library for working.
 */

/*
 * Sign in functions
 */

function requestSignin(email, passwd, callback) {
    $.ajax({
        type: "POST",
        url: "/signin",
        data: "email=" + email + "&passwd=" + passwd,
        statusCode: {
            200: function (xhr) {
                callback(xhr);
            },
            400: function (xhr) {
                alert(getObjectFromXHR(xhr).result);
            },
            403: function (xhr) {
                alert(getObjectFromXHR(xhr).result);
            }
        }
    });
}

/*
 * URL functions
 */

function getLectureUrl(lectureId) {
    if (lectureId == undefined) {
        return "/" + "lectures";
    }
    return getLectureUrl() + "/" + lectureId;
}

function getAssignmentUrl(lectureId, assignmentId) {
    if (assignmentId == undefined) {
        return getLectureUrl(lectureId) + "/" + "assignments";
    }
    return getAssignmentUrl(lectureId) + "/" + assignmentId;
}

function getSubmissionUrl(lectureId, assignmentId, submissionId) {
    if (submissionId == undefined) {
        return getAssignmentUrl(lectureId, assignmentId) + "/" + "submissions";
    }
    return getSubmissionUrl(lectureId, assignmentId) + "/" + submissionId;
}

/*
 * Get functions
 */

function getLectures(callback) {
    $.ajax({
        url: getLectureUrl(),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (lectures) {
            $.each(lectures, callback);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

function getLecture(lectureId, callback) {
    $.ajax({
        url: getLectureUrl(lectureId),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (lecture) {
            callback(lecture);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

function getAssignments(lectureId, callback) {
    $.ajax({
        url: getAssignmentUrl(lectureId),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (assignments) {
            $.each(assignments, callback);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

function getAssignment(lectureId, assignmentId, callback) {
    $.ajax({
        url: getAssignmentUrl(lectureId, assignmentId),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (assignment) {
            callback(assignment);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

function getSubmissions(lectureId, assignmentId, callback) {
    $.ajax({
        url: getSubmissionUrl(lectureId, assignmentId),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (submissions) {
            $.each(submissions, callback);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

function getSubmission(lectureId, assignmentId, submissionId, callback) {
    $.ajax({
        url: getSubmissionUrl(lectureId, assignmentId, submissionId),
        dataType: "json",
        headers: {
            Accept: 'application/json'
        },
        success: function (submission) {
            callback(submission);
        },
        error: function (xhr) {
            printError(xhr);
        }
    });
}

/*
 * Create functions
 */

function createLecture(name) {
    var lecture = {};
    lecture.name = name;
    return lecture;
}

function createAssignment(title, description) {
    var assignment = {};
    assignment.title = title;
    assignment.description = description;
    return assignment;
}

function createSubmission(description) {
    var submission = {};
    submission.description = description;
    return submission;
}

/*
 * Post functions
 */

function postLecture(lecture) {
    $.ajax({
        type: "POST",
        url: getLectureUrl(),
        data: JSON.stringify(lecture),
        success: function (result) {
            console.log(result.result);
        },
        error: function (xhr) {
            printError(xhr);
        },
        dataType: "json"
    });
}

function postAssignment(lectureId, assignment) {
    $.ajax({
        type: "POST",
        url: getAssignmentUrl(lectureId),
        data: JSON.stringify(assignment),
        success: function (result) {
            // success
            console.log(result.result);
        },
        error: function (xhr) {
            printError(xhr);
        },
        dataType: "json"
    });
}

function postSubmission(lectureId, assignmentId, submission) {
    $.ajax({
        type: "POST",
        url: getSubmissionUrl(lectureId, assignmentId),
        data: JSON.stringify(submission),
        success: function (result) {
            // success
            console.log(result.result);
        },
        error: function (xhr) {
            printError(xhr);
        },
        dataType: "json"
    });
}

/*
 * Other functions
 */

function printError(xhr) {
    console.log(xhr.statusText + ': ' + xhr.responseJSON.result);
    alert(xhr.responseJSON.result);
}