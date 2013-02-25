# VideoDialog plugin for Phonegap #

The plugin opens a fullscreen video dialog on top of the Phonegap webview.
There are no controlls. If the User touches the screen the Video will stop.
This Plugin

The Javascript running in the backround is still executing while the video is playing.
So all timer and background task are working while the video is playing.

## Adding the Plugin to your project ##

Using this plugin requires [Android PhoneGap](https://github.com/apache/incubator-cordova-android).
It is only tested for PhoneGap 2.2

1. To install the plugin, move www/video to your project's www folder and include a reference to it in your html file after phonegap.js.

    &lt;script type="text/javascript" charset="utf-8" src="phonegap.js"&gt;&lt;/script&gt;<br/>
    &lt;script type="text/javascript" charset="utf-8" src="videodialog.js"&gt;&lt;/script&gt;

2. Create a directory within your project called "src/com/phonegap/plugins/video" and move VideoPlayer.java into it.

3. In your res/xml/plugins.xml file add the following line:

    &lt;plugin name="VideoDialogPlugin" value="com.mllrsohn.plugins.videodialog.VideoDialogPlugin"/&gt;

## Using the plugin ##

The plugin creates the object `window.plugins.VideoDialog`.  To use, call the play() method:

<pre>
  /**
    * Display an intent to play the video.
    *
    * @param url           The url to play
    * @param options       JSON Object with loop false or true, default is false
    * @param successs      Callback function if the video
    * @param error         Callback function if the video
    */

    play(url, options, successs, error);

</pre>

Sample use:

    window.plugins.VideoDialog.play("http://www.server.com/file.mp4");
    window.plugins.VideoDialog.play("file:///android_asset/www/big_buck_bunny.mp4");
    window.plugins.VideoDialog.play("file:///android_asset/www/big_buck_bunny.mp4", {loop : true});
    window.plugins.VideoDialog.play("file:///android_asset/www/big_buck_bunny.mp4", {loop : true},
        function() {
            console.log('Video played until the end');
        }, function() {
            console.log('User interupted video');
        }
    );

Sample use with jQuery deferred:

    var playVideo = function() {
        var deferred = $.Deferred();
        window.plugins.VideoDialog.play("file:///android_asset/www/big_buck_bunny.mp4", {loop: false}, deferred.resolve, deferred.reject);
        return deferred.promise();
    }

    playVideo().done(function(msg) {
        console.log('Video played until the end');
    }).fail(function(msg) {
        console.log('User interupted video');
    });

Note: When playing video from the assets folder, the video is first copied to internal storage with MODE_WORLD_READABLE.

## RELEASE NOTES ##

### February 25, 2011 ###

* Initial release


## BUGS AND CONTRIBUTIONS ##
* Steffen Müller - Müller & Sohn
* Based on the [videoplayer Plugin by macdonst](https://github.com/macdonst/VideoPlayer);


## LICENSE ##

### The MIT License

Copyright (c) <2013> < Müller & Sohn Digitalmanufaktur GmbH >

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
