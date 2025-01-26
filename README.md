# # AudioCrossApp

[![GitHub](https://img.shields.io/badge/GitHub-AudioCross-blue)](https://github.com/Glasser97/AudioCross)

AudioCrossApp is a modern audio browsing and playback application designed to seamlessly connect to audio sources. With a user-friendly interface and advanced filtering capabilities, users can explore and listen to their favorite audio content. The app currently integrates with the default audio source: [asmr.one](https://asmr.one).

---

## Key Features

- **Audio Browsing**: Connect to an audio source and browse available audio tracks effortlessly.
- **Filtering and Search**: Find specific audio content through advanced filtering and search options.
- **High-Quality Playback**: Enjoy smooth audio playback with robust control features.

---

## Tech Stack

This application leverages a modern tech stack to deliver a responsive, efficient, and maintainable experience:

- **Audio Parsing & Playback**: [ExoPlayer](https://github.com/google/ExoPlayer)
- **UI Construction**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Networking**: [Ktor](https://ktor.io/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Data Persistence**: [Room](https://developer.android.com/jetpack/androidx/releases/room)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Architecture**: MVVM + Clean Architecture (Presentation, Domain, and Data layers)

---

## Highlights

The app utilizes the **MVVM + Clean Architecture**, which ensures:

1. **Separation of Concerns**: Code is divided into distinct layers:
   - **Presentation Layer**: Handles UI and user interaction.
   - **Domain Layer**: Encapsulates core business logic.
   - **Data Layer**: Manages data sources and repositories.
2. **Scalability**: Quickly swap or modify the UI or data source without affecting other components.
3. **Maintainability**: Improved testability and reduced complexity through well-defined boundaries.

---

## Roadmap

Here are the upcoming features and improvements planned for AudioCrossApp:

1. **Migration to Kotlin Multiplatform (KMP) + Compose Multiplatform**:
   - Expand platform support and reuse core logic across iOS and Android.
2. **Subtitle and Playlist Control**:
   - Introduce subtitles for audio playback.
   - Add advanced playlist management features.

---

## Contribution

We welcome contributions! If you want to contribute, please:

1. Fork the repository: [AudioCross GitHub](https://github.com/Glasser97/AudioCross)
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit your changes: `git commit -m 'Add YourFeature'`
4. Push to the branch: `git push origin feature/YourFeature`
5. Open a pull request for review.

---

## License

This project is licensed under the [MIT License](LICENSE). Feel free to use it as per the terms of the license.

---

For more information, visit the repository: [https://github.com/Glasser97/AudioCross](https://github.com/Glasser97/AudioCross).
