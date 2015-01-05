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
            200: function(xhr) {
                callback(xhr);
            },
            400: function(xhr) {
                alert(getObjectFromXHR(xhr).result);
            },
            403: function(xhr) {
                alert(getObjectFromXHR(xhr).result);
            }
        }
    });
}
