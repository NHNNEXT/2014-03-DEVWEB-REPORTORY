/*!
 * Common util library for REPORTORY
 * It needs jQuery library for working.
 */

function getObjectFromXHR(xhr) {
    return $.parseJSON(xhr.responseText);
}

function goPreviousPage() {
    if (document.referrer == "") {
        window.location = "/";
        return;
    }

    window.location = document.referrer;
}