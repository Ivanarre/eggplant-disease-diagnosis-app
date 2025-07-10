# Eggplant Disease Diagnosis Android App

A mobile-based application for diagnosing eggplant diseases using machine learning. This app helps farmers and gardeners identify diseases in eggplants through image recognition, providing quick and accurate diagnoses.

## Features

- 📸 **Real-time Disease Detection**: Take or upload photos of eggplant leaves for instant disease diagnosis
- 🔍 **ML-Powered Analysis**: Utilizes TensorFlow Lite for accurate disease classification
- 📱 **Modern UI**: Built with Jetpack Compose for a smooth, native Android experience
- 📊 **Diagnosis History**: Keep track of previous disease diagnoses
- 💾 **Offline Support**: Works without internet connection once installed
- 🌱 **Plant Care Information**: Access detailed information about eggplant diseases and care

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Machine Learning**: TensorFlow Lite
- **Database**: Room DB for local storage
- **Navigation**: Jetpack Navigation Compose
- **Image Loading**: Coil
- **Minimum SDK**: Android 10 (API Level 29)

## Screenshots

[Screenshots will be added soon]

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/Ivanarre/eggplant-disease-diagnosis-app.git
   ```

2. Open the project in Android Studio (Electric Eel or newer)

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device:
   - Minimum Android version: Android 10 (API Level 29)
   - Target Android version: Android 14 (API Level 34)

## Build Configuration

The app uses Gradle with Kotlin DSL for build configuration. Main dependencies include:
- Jetpack Compose: UI framework
- TensorFlow Lite: ML model integration
- Room: Local database
- Navigation Compose: In-app navigation
- Coil: Image loading and caching

## Project Structure

```
app/
├── src/main/
│   ├── java/com/ensias/mobile_basedeggplantcarediseasediagnosis/
│   │   ├── data/           # Database and data models
│   │   ├── ui/theme/       # App theming and styling
│   │   ├── AboutScreen.kt  # About page
│   │   ├── HomeScreen.kt   # Main screen
│   │   ├── PlantScreen.kt  # Plant diagnosis screen
│   │   └── ...
│   ├── ml/                 # ML model files
│   └── res/                # App resources
```

## License

[License information to be added]

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Contact

[Your contact information or preferred method of contact] 