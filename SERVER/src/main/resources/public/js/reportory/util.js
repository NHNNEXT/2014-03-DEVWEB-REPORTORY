/*!
 * Common util library for reportory
 * It needs jQuery library for working.
 */

function getObjectFromXHR(xhr) {
    return $.parseJSON(xhr.responseText);
}
