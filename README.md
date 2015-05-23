[![Build Status](https://travis-ci.org/fashiontec/bodyapps-android.svg?branch=master)](https://travis-ci.org/fashiontec/bodyapps-android)

# bodyapps-android

[![Join the chat at https://gitter.im/fashiontec/bodyapps-android](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/fashiontec/bodyapps-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Android app for the #bodyapps project with Gradle  

## Project Setup

Main IDE used by the team is IntelliJ-based [Android Studio](https://developer.android.com/sdk/installing/studio.html). 
To get started, download and install Android Studio, then choose "Import Project" and navigate to the project
root. Studio should import the Gradle-based structure without much ado.

### Building

* Install Google Play Services and Google repository using SDK Manager  
* Restart Android Studio
* Now you are good to run the app on a connected device

Note: for testing in an emulator, see Emulator section below.

### Eclipse/ADT?

We feel very comfortable with Android Studio so far and see no need to use ADT. If you want to use
it anyway, please feel free to contribute patches and/or setup-instructions.

### Emulator

Project requires Play Services which are not available in all versions of the Emulator. We recommend
testing on a connected device or via [GenyMotion](http://www.genymotion.com/). When running, 
GenyMotion instances act like connected devices and can be selected via ADB during launch.

To install Play Store and subsequently Play Services, follow [the instructions here](http://stackoverflow.com/questions/20121883/how-to-install-google-play-service-in-the-genymotion-ubuntu-13-04-currently-i)

### Google API

To get information from Google API it is required to create project in Google developer console.  
* Go to [Google developer console](https://console.developers.google.com/project?authuser=0) then 
  go to projects tab and click on create project.  
* Enter a project name and create a project.  
* Then go into the project and navigate to APIs under APIs & auth tab
* Turn on Google+ API
* Go to Credentials  under APIs & auth tab and click on Create new client ID
* Click on Installed Application and select Android
* Give package name as fossasia.valentina.bodyapp.main and provide your SHA1 key
  (You can get SHA1 from Eclipse via Windows > Preferences > Android > build)
* Enable deep linking and click create client ID

### Sync
		
You can test the sync options manually by cloning the web application and running it on local host if you dont want to work with online web service. Simply change the baseURL in Sync.java class according to your local host address and port.
