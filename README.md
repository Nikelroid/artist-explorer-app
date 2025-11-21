# 🎨 Artist Explorer App  


![Angular](https://img.shields.io/badge/Angular-19.2+-dd0031.svg)
![Node.js](https://img.shields.io/badge/Node.js-18.0+-339933.svg)
![Android](https://img.shields.io/badge/Android-API%2034-3DDC84.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248.svg)
![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)


**Artist Explorer** is a multi-platform application ecosystem (Web & Android) designed to help users discover artists, view detailed biographies, and explore artwork collections. The system leverages the **Artsy API** for data, **MongoDB** for persistent user data, and includes a **Generative AI** chatbot assistant.

## 📖 Description

This project provides a seamless experience for art enthusiasts across devices. It consists of three main components:
1.  **Front+Back v2 (Web):** A responsive Angular 19 application backed by a Node.js/Express server. It handles user authentication (JWT), proxies Artsy API requests, and manages user favorites.
2.  **Front(Android) v2:** A modern native Android application built with Jetpack Compose. It interfaces with the same backend to provide a synchronized mobile experience.
3.  **Experimental AI (v1):** A Python Flask service integrating Google Gemini 2.0 to act as a helpful student assistant.

## ✨ Features

### Core Functionality
* **Dynamic Search:** Real-time artist search with autocomplete functionality using the Artsy API.
* **Detailed Profiles:** View artist biographies, nationality, birth/death dates, and carousel galleries of their artworks.
* **Favorites System:** Authenticated users can save artists to their favorites list, which persists across Web and Android platforms via MongoDB.

### Web Application (Angular + Node.js)
* **Responsive Design:** Built with Bootstrap for mobile-first compatibility.
* **Secure Auth:** JWT-based authentication with HTTP-only cookies and bcrypt password hashing.
* **Interactive UI:** Tabbed navigation, modal views for artwork categories, and toast notifications.

### Android Application (Kotlin + Compose)
* **Modern UI:** Native Jetpack Compose interface with Material 3 design.
* **Session Management:** Persistent login using cookies and SharedPreferences.
* **Optimized Networking:** Uses Retrofit and Coil for efficient API calls and image loading.

## 🛠️ Installation

### Prerequisites
* **Node.js** (v18+) and **npm**
* **Android Studio** (Koala or later recommended, API 34 SDK)
* **Python** (3.10+)
* **MongoDB Atlas** URI
* **Artsy API** Credentials
* **Google Gemini API** Key

### 1. Web Application (Front+Back v2)
Navigate to the web directory and install dependencies:
```bash
cd "Front+Back v2"
npm install
````

Configure your environment variables (create a `.env` file) with your MongoDB URI and Artsy credentials. Then run:

```bash
# Development server
npm start
```

*The Angular frontend is served via the Node backend or can be run separately via `ng serve`.*

### 2\. Android Application

1.  Open Android Studio.
2.  Select **Open** and navigate to `Front(Android) v2`.
3.  Allow Gradle to sync dependencies.
4.  Create an emulator (Pixel 8 Pro, API 34 recommended) or connect a physical device.
5.  Click **Run**.

### 3\. AI Assistant Service (Front+Back v1)

If running the Python AI component:

```bash
cd "Front+Back v1"
pip install -r requirements.txt
python main.py
```

## 🚀 Usage

1.  **Register/Login:** Create an account to enable the "Favorites" and "Similar Artists" features.
2.  **Search:** Use the search bar to find artists (e.g., "Picasso", "Warhol").
3.  **Explore:** Click on an artist to view their bio and scroll through their artwork.
4.  **Manage Favorites:** Click the Star icon to add/remove artists from your dashboard.

## 🤝 Contributing

Contributions are welcome\! Please follow these steps:

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

For support or queries, please open an issue on this repository.

```
```
