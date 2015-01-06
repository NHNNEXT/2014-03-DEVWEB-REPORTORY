/*!
 * Upload library for REPORTORY
 */

var maxRequestSize = 1024 * 5;

function uploadFile(f) {
    var url = "/attachments";
    var attachmentInfo = {};
    attachmentInfo.name = f.name;
    attachmentInfo.size = f.size;
    if (!attachmentInfo.name)
        attachmentInfo.name = "uploadFile";

    var param = JSON.stringify(attachmentInfo);

    var req = new XMLHttpRequest();
    req.open("POST", "/attachments", true);
    req.setRequestHeader("Content-Type", "application/json");
    req.onreadystatechange = function () {
        if (req.readyState != 4) return;
        console.log(req);
        if (req.status != 200) {
            alert("Error code:" + req.status);
            return;
        }
        var uploadUrl = req.responseText;
        uploadFragments(uploadUrl, f);
    };
    req.send(param);
}

function uploadFragments(url, f) {
    console.log("upload to " + url);
    var fsize = f.size;
    var i = 0;
    var req = new XMLHttpRequest();
    req.open("POST", url, false);
    for (i = 0; i < fsize; i += maxRequestSize) {
        var last = i + maxRequestSize - 1;
        console.log("sending " + i + " to " + last);
        if (maxRequestSize > fsize - i) {
            last = fsize - 1;
        }
        var trans = f;
        if (i != 0 || last != fsize)
            trans = f.slice(i, last + 1, "application/octet-stream");
        req.open("POST", url, false);
        req.setRequestHeader("Content-Range", "bytes " + i + "-" + last + "/" + fsize);
        req.send(trans);
        if (req.status != 200) {
            alert("Error on sending " + f.name);
            return;
        }
    }

    console.log("fin send");
    req.open("POST", url, false);
    req.setRequestHeader("Content-Range", "bytes " + fsize + "-" + fsize + "/" + fsize);
    req.send("");
    if (req.status != 200) {
        alert("Error on sending " + f.name);
        return;
    }
    alert(req.responseText);
}
