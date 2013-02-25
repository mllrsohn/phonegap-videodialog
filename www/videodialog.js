cordova.define("cordova/plugin/videodialog",
  function(require, exports, module) {
    var exec = require("cordova/exec");
    var VideoDialog = function () {};

    VideoDialog.prototype.play = function(url, options, successCallback, errorCallback) {
        if (!options) {
            options = {};
        }
        var params = {
            url: url,
            loop : (options.loop === true ? true : false)
        };

        if (typeof errorCallback !== "function")  {
            errorCallback = function() {};
        }

        if (typeof successCallback !== "function")  {
            errorCallback = function() {};
        }

        if (url === undefined)  {
            console.log("VideoView.play failure! Please specify a url");
            return false;
        }

        exec(successCallback, errorCallback, "VidDialog", "play", [params]);
    };

    var videodialog = new VideoDialog();
    module.exports = videodialog;
});

if (!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.VideoView) {
    window.plugins.VideoDialog = cordova.require("cordova/plugin/videodialog");
}