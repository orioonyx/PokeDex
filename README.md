# PokeDex 🐾

![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
![Platform](https://img.shields.io/badge/platform-Android-blue)

A modern Pokemon Encyclopedia application built with Kotlin, implementing **Clean Architecture** and the **MVVM Design Pattern**.  
PokeDex leverages the power of **Jetpack components**, **Room**, and **Flow** for a seamless and modularized experience.

![](https://github.com/user-attachments/assets/7bccaaa7-4d37-42ae-9ae1-7697366af454)


## Features Overview 🛠️

- **Clean Architecture**
    - Clear separation of concerns with `app`, `data`, and `domain` modules, ensuring maintainability and scalability.

- **MVVM Design Pattern**
    - Decouples the View and ViewModel for better organization of UI logic and business rules.

- **Offline-First Approach**
    - Data persistence using **Room** for Pokémon details and caching.

- **Jetpack Components**
    - **ViewModel**: Manages UI-related data lifecycle-aware for efficient state management.
    - **Room**: Provides a robust local database with type-safe queries.
    - **LiveData**: Observes changes in UI-related data seamlessly.
    - **Palette**: Extracts prominent colors from images for dynamic UI design.

- **Data Binding & View Binding**
    - Simplifies UI updates by directly binding UI components to data and reduces boilerplate code for view references.

- **Reactive Programming**
    - Powered by **Kotlin Coroutines & Flow** for handling asynchronous tasks and state management efficiently.

- **Networking**
    - **Retrofit & OkHttp**: Fast and reliable networking stack for API communication.
    - **Moshi**: JSON parsing library integrated with Retrofit for seamless data handling.

- **Dependency Injection**
    - Using **Hilt** for scalable and testable dependency injection.

- **Image Loading**
    - **Glide**: Efficient image loading and caching library for handling Pokémon images.

- **Logging**
    - **Timber**: Lightweight and extensible logging library for debugging.

- **Comprehensive Testing**
    - **JUnit**, **Kotest**, **MockK**: For unit testing.
    - **Espresso**: For UI testing.
    - **Hilt Testing**: Simplified dependency injection in tests.
    - **Coroutine Testing**: For verifying suspend functions and flows.


## Architecture ✨
![](https://github.com/user-attachments/assets/3ee0c157-f432-45ce-adb7-21bebded0f28)


## Preview 🚀
<img src="https://github.com/user-attachments/assets/17bc6ac5-fc92-4c34-8c12-db9083962a02" style="width: 260px; height: auto;" />


## Open API 🌍
<img src="https://user-images.githubusercontent.com/24237865/83422649-d1b1d980-a464-11ea-8c91-a24fdf89cd6b.png" align="right" width="100px"/>
PokeDex uses [PokeAPI](https://pokeapi.co/) for fetching data related to Pokémon.


## License 📜
```xml
Designed and developed by 2024 kyungeun noh

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
