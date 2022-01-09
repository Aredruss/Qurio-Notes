
![](./assets/banner_big.png)

# Qurio Notes - A minimalistic notepad

## About
This project was created to explore my own UI/UX design capabilities.
It offers clean and efficient functionality, and is designed to distract the user as little as possible.

## 	:city_sunrise: Day Mode 
Home | Create a note | Edit a note | About | Share or save a note
--- | --- | --- | --- | --- 
![](./assets/light_home.png) | ![](./assets/light_create.png) | ![](./assets/light_note.png) | ![](./assets/light_about.png) | ![](./assets/light_share.png)

<br />

## :city_sunset: Dark mode
Home | Create a note | Edit a note | About | Share or save a note
--- | --- | --- | --- | --- 
![](./assets/dark_home.png) | ![](./assets/dark_create.png) | ![](./assets/dark_note.png) | ![](./assets/dark_about.png) | ![](./assets/dark_share.png)
<br />


## :building_construction: Built With 
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Koin](https://insert-koin.io/) - A smart Kotlin dependency injection librar  
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous work and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collection of libraries that help you design robust, testable, and maintainable apps.
    - [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - A flow is an asynchronous version of a Sequence, a type of collection whose values are lazily produced.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
    - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
    - [Jetpack Navigation](https://developer.android.com/guide/navigation) - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Figma](https://figma.com/) - Figma is a vector graphics editor and prototyping tool which is primarily web-based.

<br />

## :japanese_castle: Package Structure 

    com.aredruss.qurio # Root Package
    ├── di                  # Koin DI Modules 
    ├── domain              # Local Data Storage
    │   ├── database        # Database Instance and the Data Access Object for Room
    ├── model               # Model classes [Notes]
    ├── repo                # Used to handle all data operations
    ├── view                # Activity/Fragment View layer
    │   ├── about           # App's summary
    │   ├── home            # App's Home
    │   ├── notes           # Create and Edit notes
    │   ├── utils           # Base classes and extensions
    ├── helpers             # All extension functions and utilities


<br />


## :house: Architecture 
This app uses [MVVM](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://github.com/TheCodeMonks/Notes-App/blob/master/screenshots/ANDROID%20ROOM%20DB%20DIAGRAM.jpg)

## :clap: Credits 

- Special Thanks to [gmk57](https://github.com/gmk57) for all the cool gists

<br />

## :open_book: License 
```
    Apache 2.0 License


    Copyright 2021 Alexander Medyanik

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

```