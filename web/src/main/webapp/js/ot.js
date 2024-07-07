function createObject(proto) {
    function ctor() {
    }
    ctor.prototype = proto;
    return new ctor();
}

function makeDivSuccess(objId) {
    $("#" + objId).removeClass("has-error has-warning");
    $("#" + objId).addClass("has-success has-feedback");
}

function makeDivError(objId) {
    $("#" + objId).removeClass("has-success has-warning");
    $("#" + objId).addClass("has-error has-feedback");
}

function makeDivWarning(objId) {
    $("#" + objId).removeClass("has-error has-success");
    $("#" + objId).addClass("has-warning has-feedback");
}

function bindDateFields(dtFormat) {
    $(".DateField").datepicker({dateFormat: dtFormat});
    $(".DateField").attr("placeholder", dtFormat.toUpperCase());
}

function blockUI() {
    $.blockUI({
        message: $('#waitMessage'),
        overlayCSS: {
            backgroundColor: '#fff',
            opacity: 0.6,
            cursor: 'wait'
        }
    });
}

function unblockUI() {
    $.unblockUI();
}

function handleAjax(data) {
    var status = data.status;
    switch (status) {
        case "begin":
            blockUI();
            break;

        case "complete":
            unblockUI();
            break;
    }
}

function handleAjaxError(data) {
    if (data.type !== "event") {
        alert(data.errorMessage);
    }
}

function parseDate(date) {
    var origParse = Date.parse, numericKeys = [1, 4, 5, 6, 7, 10, 11];
    var timestamp, struct, minutesOffset = 0;
    struct = /^(\d{4}|[+\-]\d{6})(?:-(\d{2})(?:-(\d{2}))?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(?:\.(\d{3}))?)?(?:(Z)|([+\-])(\d{2,4})(?::(\d{2}))?)?)?$/.exec(date);
    if (struct) {
        for (var i = 0, k; (k = numericKeys[i]); ++i) {
            struct[k] = +struct[k] || 0;
        }

        struct[2] = (+struct[2] || 1) - 1;
        struct[3] = +struct[3] || 1;
        if (struct[8] !== 'Z' && struct[9] !== undefined) {
            minutesOffset = struct[10] * 60 + struct[11];
            if (struct[9] === '+') {
                minutesOffset = 0 - minutesOffset;
            }
        }
        timestamp = Date.UTC(struct[1], struct[2], struct[3], struct[4], struct[5] + minutesOffset, struct[6], struct[7]);
    }
    else {
        timestamp = origParse ? origParse(date) : NaN;
    }
    return timestamp;
}

/** 
 * Retricts input for ID or code fields. Restriction pattern is [0-9A-Za-z_\-] 
 * @param e Event object from input field 
 */
function restrictInputForIds(e) {
    var pattern = /[0-9A-Za-z_\-]/g;
    return restrictInput(e, pattern);
}

/** 
 * Retricts input for numbers. Restriction pattern is [0-9\.] 
 * @param e Event object from input field 
 */
function restrictInputDouble(e) {
    var pattern = /[0-9\.]/g;
    return restrictInput(e, pattern);
}

/** 
 * Retricts input for numbers. Restriction pattern is [0-9\.] 
 * @param e Event object from input field 
 */
function restrictInputInteger(e) {
    var pattern = /[0-9]/g;
    return restrictInput(e, pattern);
}

/** 
 * Retricts input for input fields.
 * @param e Event object from input field 
 * @param pattern Regular expression pattern to use for restriction.
 */
function restrictInput(e, pattern) {
    var keyCode = e.keyCode === 0 ? e.charCode : e.keyCode;
    return (String.fromCharCode(keyCode).match(pattern) ||
            ((e.keyCode === 8 || e.keyCode === 9 || e.keyCode === 46 ||
                    e.keyCode === 36 || e.keyCode === 35 || e.keyCode === 37 ||
                    e.keyCode === 39) && e.charCode !== e.keyCode));
}