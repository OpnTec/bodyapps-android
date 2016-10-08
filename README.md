[![Build Status](https://travis-ci.org/fossasia/bodyapps-android.svg?branch=master)](https://travis-ci.org/fossasia/bodyapps-android)

# bodyapps-android

[![Join the chat at https://gitter.im/fossasia/bodyapps-android](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/fossasia/bodyapps-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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


# Features of the app
* Can create and store measurement records.
* Can add images and notes to measurement records.
* Can sync measurement records with the server.
* Can download HDF file for measurement record.


# Packages
There are five folders (or packages) in the org.fashiontec.bodyapps package.
* db- Contains the classes that deal with creation of local database.
* main- Contains the classes dealing with screens of app
* models- Entity classes
* managers - Contains the classes dealing with database calls and connects entities with database.
* sync – Contains classes that deal with syncing measurement records with server.


# Activities
There are five main screens in the app. And there is an activity for each main screen. All of these activities are in the package org.fashiontec.bodyapps.main

## MainActivity
This is the landing activity of the app. Contains buttons which leads to other screens such as saved, create and settings. If there is no user logged in it generates an alert dialog and disable create and saved buttons. At the startup it initiates sync process if Auto sync is turned on.
### Methods :
* dialog(String title, String message) - Shows a alert dialog with given title and message
* onClick(View v) [override] - Handles the clicks of every Button
* onResume() [override] – Disable and enable buttons according to user’s log in state.

## SettingsActivity
This activity provides the screen to log in using Google. It takes user’s name and email from G Plus API and sends it to BodyApps server to create account for user and then get a ID for user. Then it saves those data in local database. When user is logged in, it displays user’s picture from G Plus, user’s name and user’s email. And it will display option to turn auto sync on or off.
### Methods :
* signInWithGplus() - Sign in the user to Google plus
* signOutFromGplus() - Sign out user from Google plus
* revokeGplusAccess() - Revoking access from Google plus
* updateUI(boolean isSignedIn) - Update the UI according to signed in or not
* getProfileInformation() – get profile info from G Plus when connected.
* onConnectionFailed(ConnectionResult result) [override]  -  Try to establish connection if failed
* onConnected(Bundle arg0) [override]  -  After connecting to Google invoke getProfileInformation(), and initiate connection to BodyApps server.
### Async Tasks :
* LoadProfileImage – Loads profile image from G Plus.
* HttpAsyncTaskUser – This task sends user data to BodyApps server and saves user details in local database according to server reply.

## CreateActivity
This activity provides screen to create a measurement record. It is a simple activity with no http calls and only contains two edit texts, two spinners and date picker dialog.

### Methods :
* updateLabel() – Update the date of birth label when a date is selected
* setData() – Check the validity of data entered by user.
* setMeasurement(String name, String email) - Creates a measurement object and person object using entered data and adds the person to database.
* getID() – Generate a UUID to assign as measurement ID
* closer() - Closes current view and opens MeasurementActivity
* dialog(String title, String message) - Shows a alert dialog with given title and message
 

## MeasurementActivity
This is the activity which handles adding measures to measurements. This is bit complex and contains one static activity and number of Fragments. If device is portrait mode the screen will only show icon grid and by clicking respective measurement set will open. If device is in landscape mode icon grid and measurement sets will be displayed side by side.

### Methods:
* DBSaver(Context context) - Saves the measurement to the database

### GridFragment
This Fragment handles the grid view which shows icons which lead groups of measures.

### Methods in GridFragment :
* viewSet(View view) - Sets the view according to the orientation of the device
* filledSet() – Sets the number of fields filled in each measures group
* chooseView(int index) – Choose which Fragment to load according to the clicked icon in icon grid.

### ItemActivity
Supporting activity which loads measurement set fragments when device in portrait mode.

Head, Neck, Shoulder, Chest, Arm, Hand, Leg, Trunk and Heights measurement groups or measurement sets. Each one these are Fragments and contains edit texts where user can enter respective measurements. Some of them contains spinners. These fragments will be loaded directly to MeasurementActivity if device is in landscape mode. Else they will be loaded to ItemActivity.
### Pics
This fragment deals with adding pictures to measurement record. It contains three buttons Front, Side and back. Clicking these buttons will open a dialog where user will be able select their image source or they can remove existing picture.
### Methods in Pics :
* captureImage() - Open camera to capture an image
* openImage() - Open gallery to choose a image
* removeImage() - Removes a added image
* getOutputMediaFileUri() - Gives output image file path as a Uri
* getOutputMediaFile() - Creates output image file folder and creates file path based on file type
* previewCapturedImage() - Previews selected or captured image
* compress(Uri inFile, Uri outFile) - Compress the image
* dialog() - Display dialog with image options such as camera or from file

### Notes
This Fragment handles the additional notes added to measurement record.


## SavedActivity
This activity shows the list of measurements current user has created and it allows options such as edit, delete and getting HDF. This activity contains two Fragments and one static Activity.  If device is in portrait mode the screen will only show list of measurements and by clicking respective measurement it will show the contents.. If device is in landscape mode list and measurement contents will be displayed side by side.

### Methods:
* edit(String ID) - Go to edit View
* options(final String ID) - Pops a warning dialog before delete a measurement
* delete(String ID) - Deletes the given measurement
* getHDF() - Manages the HDF getting process
* getPath(String name, String measurementID) - Gives a path to save the HDF
* mail(String path) - Opens email dialog with HDF attached
* getDataForListView(Context context) - Gets a List of measurementLisModel objects from database to populate measurement list

### Async Tasks :
* DownloadFileAsync - Async task to download the HDF

### SavedList
This Fragment contains the list that shows measurements.

### Methods in SavedList :
* viewSet(View view) - Checks for the device’s orientation and adjust the screen according to it.

### ViewSavedActivity
If device is in portrait mode this static Activity will load ViewSavedFragment and will show the contents of the measurement selected from list.

### Methods in ViewSavedActivity:
* edit(String ID) - Go to edit View
* options(final String ID) - Pops a warning dialog before delete a measurement
* delete(String ID) - Deletes the given measurement

### ViewSavedFragment
This Fragment has bunch of labels in it’s layout and displays contents of selected measurement. It gets loaded to ViewSavedActivity if device is in portrait mode or to SavedActivity if device is in landscape mode.


# Other Classes

## org.fashiontec.bodyapps.db
* DatabaseHandler - Creates the local database in the device.
* DBContract - This abstract class Contains all the constants used as table names and attributes in database.
* DataContentProvider - Stub content provider used to create Sync Adapter

## org.fashiontec.bodyapps.main
* SavedAdapter - Adapter which populates the saved measurements list.
* GridAdapter - Adapter which handles and populates the icon grid in Measurement activity.

## org.fashiontec.bodyapps.managers
* MeasurementManager - Manages the requests to measurements table and delete table.
* PersonManager - Manages the requests to person table.
* UserManager - Manages the requests to user table.

## org.fashiontec.bodyapps.models
* Measurement - Entity class for measurement object.
* MeasurementListModel - Entity class for measurement model that used to populate saves measurement list. Only contains few subset of attributes of Measurement object.
* Person - Entity class for person  object.
* User - Entity class for user  object.

## org.fashiontec.bodyapps.sync
* Auth - Stub authenticator for Sync Adapter.
* HDF- Manages getting HDF from server.
* Sync - Contains post, get, put and delete methods used to sync measurements.
* SyncAdapter - Manages automatic sync of measurements.
* SyncAuth - Service for stub authenticator.
* SyncMeasurement - Manages sending and getting measurements to server.
* SyncPic - Manages sync of attached pictures.
* SyncService - Service for sync adapter.
* SyncUser - Manges sending user ID to server and getting an ID.


# XML files

## layout

* activity_main.xml - Landing screens layout
* activity_create.xml - Layout of the crete activity
* activity_item.xml - Layout for ItemActivity load measurement set fragments when device is potrait.
* activity_measurement.xml - Layout for MeasurementActivity
* activity_saved.xml - Layout for SavedActivity
* activity_settings.xml - Layout for SettingsActivity
* activity_view_saved.xml - Layout for ViewSaved activity to display contents of measurement record.
* arm.xml - Contains screen for measurements related to arm and inflated by Arm Fragment.
* chest.xml - Contains screen for measurements related to chest and inflated by Chest Fragment.
* choose_image_dialog.xml - Layout for image options dialog in Pics Fragment.
* foot.xml - Contains screen for measurements related to foot and inflated by Foot Fragment.
* fragment_measurement.xml - Layout for GridFragment and contains icon grid.
* fragment_saved.xml - Layout for SavedList Fragment and contains list.
* fragment_view_saved.xml - Layout for ViewSavedFragment to display contents of measurement record.
* grid_cell.xml - Layout of one cell in icon grid.
* hand.xml - Contains screen for measurements related to hand and inflated by Hand Fragment.
* head.xml - Contains screen for measurements related to head and inflated by Head Fragment.
* heights.xml - Contains screen for measurements related to heights and inflated by Heights Fragment.
* hip_and_waist.xml - Contains screen for measurements related to hip and waist inflated by HipAndWaist Fragment.
* leg.xml - Contains screen for measurements related to leg and inflated by Leg Fragment.
* neck.xml - Contains screen for measurements related to neck and inflated by Neck Fragment.
* notes.xml - Layout for Notes Fragment
* pics.xml - Layout for Pics Fragment
* saved_tab.xml - Layout for single stripe in measurement list.
* shoulder.xml - Contains screen for measurements related to shoulder and inflated by Shoulder Fragment.
* trunk.xml - Contains screen for measurements related to trunk and inflated by Trunk Fragment.


## layout-land

* activity_main.xml - Landing screens layout for landscape mode
* activity_measurement.xml - Layout for MeasurementActivity when in landscape mode.
* activity_saved.xml - Layout for SavedActivity when in landscape mode.
* fragment_measurement.xml - Layout for GridFragment when in landscape mode.

## menu

* saved_context.xml - Context menu for measurements list in SavedActivity.
* saved_options.xml - Options menu for SavedActivity when in portrait mode.
* saved_options_land.xml - Options menu for SavedActivity when in lanscape mode.
* view_saved.xml - Options menu for ViewSavedFragment.

## values

* arm_type.xml - Populates the arm type spinner in Arm Fragment.
* back_shape.xml - Populates the back shape spinner in Trunk Fragment.
* chest_types.xml - Populates the chest type spinner in Chest Fragment.
* gender.xml - Populates the gebder spinner in CreateActivity.
* shoulder_types.xml - Populates the shoulder type spinner in Shoulder Fragment.
* stomach_shape.xml - Populates the stomach shape spinner in Trunk Fragment.
* units.xml - Populates the unit spinner in CreateActivity.

## drawable-nodpi

* btn_txt.xml - Defines the behavior and appearance of button text.
* ios_btn.xml - Defines the behavior and appearance of buttons.
* lst_txt.xml - Defines the behavior and appearance of text in saved measurements list.
* round_corner.xml - Background for cells in icon grid. Defines their border with round corners.
* round_corner_color.xml - Background for titles in ViewSavedFragment.
* saved_list_selector.xml - Defines the selector of saved measurements list.


# Local database

## ERD
![](http://i.imgur.com/IuZcGTn.png)

## Data

### Person
* id- Person id generated by database.
* name- Name of the person.
* gender - Gender of the person.
* email - email  of the person.
* dob - Date of birth  of the person.

### User
* id - Id given by the server.
* name - Name of the user.
* email - email of the user.
* auto_sync - If user has turned on auto sync option or not.
* sync - Last sync time.
* current_user - If this user is the current user of the app.

### Delete
* file_id - IDs of deleted measurements to be deleted from server too.

### Measurement
* id - ID of the measurement.
* user_id - User who created measurement.
* person_id - Person who belongs measurements.
* created - Date created.
* unit - Unit of the recorded measurements.
* is_synced - If measurement is synced since last edit.
* sync - If measurement is synced with server at least once.  
**There are number of other attributes in measurement table and all of them are measurements related to various body parts.**
