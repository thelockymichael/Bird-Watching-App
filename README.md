# Bird-Watching-App
This project is created in Android Studio for the CGI Future Talent's preassignment. 

## Description
The project has two activity screens, MainActivity and DetailsActivity. MainActivity is for sorting, adding and displaying the list and DetailsActivity is to fill in bird observation details.
The few important classes are DBHelper class that implements SQLiteOpenHelper interface for saving and sorting data structures and CustomAdapter that allows to use layout resource files for versatile list item customisations. 


### Running and building the application

<details><summary><b>Show instructions</b></summary>

1. Download or clone this GitHub repository.

2. Open the downloaded project in Android Studio (3.5.3v at the time of uploading) 


#### Here I have listed how to start running and building the app into an APK.

* <b> Running project. </b>
Running project will launch the application on an emulated or physical Android device.
In the image the current emulating device is set to Pixel 3 XL.
<p align="center">
  <img src="readme_images/play_circle.jpeg" alt="play project" width="650">
</p>

* <b> Building project. </b>
Builds an APK of all modules in the current project for their selected variant. When IDE finishes building, a confirmation notification appears, providing a link to the APK file. The path to file is in <i><b>BirdApp/app/build/outputs/apk/debug/</b></i> and default file name is app-debug.
<p align="center">
  <img src="readme_images/build_circle.jpeg" alt="build project" width="650">
</p>

* <b> Make project. </b>
Make project compile all the source files in the entire project that have been modified since the last compilation are compiled. 
Dependent source files, if appropriate, are also compiled.
<p align="center">
  <img src="readme_images/make_circle.jpeg" alt="make project" width="650">
</p>

</details>

### Automation testing
Not implemented :( 

### Application screenshots

<p align="center">
  <img src="readme_images/main_view.png" alt="details" width="300">
  <img src="readme_images/details_view.png" alt="details" width="300">
  <img src="readme_images/maps_view.png" alt="details" width="300">
</p>

### Change log
See CHANGELOG [here](CHANGELOG.md)
