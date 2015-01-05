/*!
 * Common util library for REPORTORY
 * It needs jQuery library for working.
 */

function getObjectFromXHR(xhr) {
    return $.parseJSON(xhr.responseText);
}
