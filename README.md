[![Build Status](https://travis-ci.org/fashiontec/bodyapps-android.svg?branch=master)](https://travis-ci.org/fashiontec/bodyapps-android)

bodyapps-android
=======================
Android app for the #bodyapps project with Gradle  

IDE Setup
---------
Main IDE used by the team is the new, IntelliJ-based [Android Studio](https://developer.android.com/sdk/installing/studio.html). 
To setup, download and install Android Studio, choose "Import Project" and navigate to the project
root. Studio should import the Gradle-based structure without much ado.

#### Building

1.  Install Google Play Services and Google repository using SDK Manager  
2.  Restart Android Studio
3.  Now you are good to run the app on a connected device

Note: for testing in an emulator, see Emulator section below.

### Eclipse/ADT?

We feel very comfortable with Android Studio so far and see no need to use ADT. If you want to use
it, please feel free to contribute patches and/or setup-instructions.

Emulator
--------
Project requires Play Services which are not available in all version of the Emulator. We recommend
testing on a connected device or via [GenyMotion](http://www.genymotion.com/). When running, 
GenyMotion instances act like connected devices and can be selected via ADB when launching.

To install Play Store and subsequently Play Services, follow [the instruction here](http://stackoverflow.com/questions/20121883/how-to-install-google-play-service-in-the-genymotion-ubuntu-13-04-currently-i)

Google API
----------
To get information from Google API it is required to create project in Google developer console.  
1. Go to [Google developer console](https://console.developers.google.com/project?authuser=0) and 
   go to projects tab and click on create project.  
2. Give a project name and create a project.  
3. Then go in to the project and go to APIs under APIs & auth tab.  
4. Turn on Google+ API.  
5. Go to Credentials  under APIs & auth tab and click on Create new client ID.  
6. Click on Installed Application and select Android.  
5. Give package name as fossasia.valentina.bodyapp.main and give your SHA1 key.  
   (You can get SHA1 from Eclipse by Windows> Preferences > Android > build )  
6. Enable deep linking and click create client ID.

Sync
----		
**NOTE: Web application Running in the localhost and listening to port 8020 is essential if you try sync options. Otherwise App is fully functional offline**  
And you can change the IP and URL according to your system by changing:  
URL in SyncUser class(line:24)  
URL in SyncMeasurement class(line: 19)  
parameter in SettingsActivity class (line:280)  
parameter in MeasurementActivity class (line: 166)
