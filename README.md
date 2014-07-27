[![Build Status](https://travis-ci.org/fashiontec/bodyapps-android.svg?branch=master)](https://travis-ci.org/fashiontec/bodyapps-android)

bodyapps-android
=======================
Android app for the #bodyapps project with Gradle  

IDE Setup
---------
Main IDE used by the team is the new, IntelliJ-based [Android Studio](https://developer.android.com/sdk/installing/studio.html). 
To setup, download and install Android Studio, choose "Import Project" and navigate to the project
root. Studio should import the Gradle-based structure without much ado.

### Eclipse/ADT?

We feel very comfortable with Android Studio so far and see no need to use ADT. If you want to use
it, please feel free to contribute patches and/or setup-instructions.

Testing
-------
Real device, Emulator or your personal copy of [GenyMotion](http://www.genymotion.com/), if you 
happen to have one.

Building the project:
---------------------  
The recommended IDE is Android Studio. The steps to build project in it are described below.  
If you are using any other IDE treat the project as any other Gradle project.  
But you may have to install Android SDK manually.  
**Steps to build the project in Android Studio**  
1.	File> Import project and select the cloned project.  
2.	Wait till Gradle downloads the dependencies and try to build the project.(most probably it will fail)  
3.	Go to SDK manger by clicking the icon on tool bar.  
4.	Install Google Play Services and Google repository.  
5.	Restart Android Studio.  
6.	Now you are good to run the app on a connected device.   
	But if you want to go with a emulator you cant use AVD because it doesn't support Google play services.  
		i. Download GennyMotion emulator from here : http://www.genymotion.com/ (it's free for personal use)  
		ii.Download a device to GennyMotion and install Google play services(You can follow this thread: http://stackoverflow.com/questions/20121883/how-to-install-google-play-service-in-the-genymotion-ubuntu-13-04-currently-i)  
		iii. Log in to device using a Google account.  
		iv. Run the app while virtual device is on. It will automatically choose the device.  


**Creating Google API project**  
To get information from Google API it is required to create project in Google developer console.  
1. Go to Google developer console(https://console.developers.google.com/project?authuser=0) and go to projects tab and click on create project.  
2. Give a project name and create a project.  
3. Then go in to the project and go to APIs under APIs & auth tab.  
4. Turn on Google+ API.  
5. Go to Credentials  under APIs & auth tab and click on Create new client ID.  
6. Click on Installed Application and select Android.  
5. Give package name as fossasia.valentina.bodyapp.main and give your SHA1 key.  
(You can get SHA1 from Eclipse by Windows> Preferences > Android > build )  
6 . Enable deep linking and click create client ID.
		
**NOTE: Web application Running in the localhost and listning to port 8020 is essential if you try sync options. Otherwise App is fully functional offline**  
And you can change the IP and URL according to your system by changing:  
URL in SyncUser class(line:24)  
URL in SyncMeasurement class(line: 19)  
parameter in SettingsActivity class (line:280)  
parameter in MeasurementActivity class (line: 166)

