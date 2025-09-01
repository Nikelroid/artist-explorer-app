# 🎨 Artist Explorer App  
**CSCI 571 – Spring 2025 | Android Development Project**

An Android application built with **Kotlin, Jetpack Compose, Retrofit, Coil, OkHttp, and Artsy API**.  
The app allows users to **search artists, view detailed artist information, explore artworks, and manage favorites** with **persistent login support**.

---

## 📌 Features

### 🔹 App Flow
- **Splash Screen & App Icon** – Smooth startup experience with branding.  
- **Home Screen**  
  - Displays current date.  
  - Favorites Section (based on login state).  
  - Search + User icons in toolbar.  
  - "Powered by Artsy" link (opens Artsy homepage).  
- **Search Functionality**  
  - Search artists dynamically after typing 3+ characters.  
  - Results update automatically via Artsy API.  
  - Clean list with images (loaded via Coil).  
- **Search Result Screen**  
  - Scrollable list of artists.  
  - Close button to clear input.  
- **Detailed Artist Screen**  
  - Progress bar while fetching data.  
  - **Tabs**:  
    - *Details* → Biography, nationality, birthday, deathday.  
    - *Artworks* → Artwork cards with name + image + categories dialog (carousel loop).  
    - *Similar* → Related artists (only when logged in).  

### 🔹 Authentication
- **Login & Register Screens** – Built with Jetpack Compose, including:  
  - Form validation.  
  - Hidden password input.  
  - Loading states + error messages.  
  - Snackbar messages on success/failure.  
- **Persistent Login with Cookies** – Using `PersistentCookieJar` + SharedPreferences.  
- **User Avatar Dropdown** (on Home Screen when logged in):  
  - Log Out (with snackbar).  
  - Delete Account (with snackbar).  

### 🔹 Favorites
- Users can **add/remove artists** from favorites.  
- Favorite state synced with backend (MongoDB Atlas).  
- Snackbar messages for feedback (Added / Removed).  
- Quick navigation to artist details via arrow button.  

---

## 🛠️ Tech Stack

- **Languages**: Kotlin (Jetpack Compose), Java (optional)  
- **Backend**: Node.js (from Assignment 3), MongoDB Atlas  
- **APIs**: [Artsy API](https://developers.artsy.net/)  
- **Libraries & Tools**:  
  - [Android Studio](https://developer.android.com/studio) (Pixel 8 Pro Emulator, API 34)  
  - [Retrofit](https://square.github.io/retrofit/) – HTTP requests  
  - [OkHttp](https://square.github.io/okhttp/) – Networking + cookies  
  - [Coil](https://coil-kt.github.io/coil/) – Image loading  
  - [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar) – Session management  
  - [Material Design 3](https://m3.material.io/) – UI components  

---

## 📲 Screens Overview

| Screen | Description |
|--------|-------------|
| Splash Screen | Shows app logo on launch |
| Home | Date, favorites section, search & user icons |
| Search | Dynamic search (3+ chars), artist list with images |
| Search Results | Scrollable artist cards, clearable input |
| Artist Details | Tabs: Details / Artworks / Similar (logged in only) |
| Login | Email & password validation, snackbar on success/failure |
| Register | Full name, email, password, snackbar on success |
| Favorites | Add/remove artists, persistent storage via backend |

---

## 🚀 How to Run

1. **Clone this repo**  
   ```bash
   git clone https://github.com/<your-username>/artist-explorer-app.git
   cd artist-explorer-app
   ```

2. **Open in Android Studio**  
   - Use the latest version of Android Studio.  
   - Select **Pixel 8 Pro Emulator with API 34** for best results.  

3. **Set up backend (Assignment 3)**  
   - Ensure your Node.js backend from Assignment 3 is running.  
   - Backend should be connected to **MongoDB Atlas** for authentication + favorites.  

4. **Run the app**  
   - Build and launch from Android Studio.  
   - Test login, registration, search, favorites, and detail screens.  

---

## 📹 Demo

A full walkthrough video is provided as part of the course submission.  
👉 [Reference Demo Video](https://www.youtube.com/watch?v=Si0QBggemRA)

---

## 📷 Screenshots (Coming Soon)

---

## ✨ Credits

- **Instructor**: Marco Papa  
- **APIs**: [Artsy](https://www.artsy.net/)  
- **Course**: USC CSCI 571 – Web Technologies
