# 🎨 Artist Explorer App  
**CSCI 571 – Spring 2025 | Full-Stack Development Portfolio**

A comprehensive artist exploration platform built across **three progressive assignments** showcasing modern web and mobile development skills.  
Features **artist search, detailed information, artwork galleries, and user authentication** using the **Artsy API ecosystem**.

---

## 📁 Repository Structure


```
artist-explorer-app/
├── Front(Android) v2/          # Flask Web App (Vanilla JS)
├── Front+Back v2/          # Angular Web App (Full-Stack)
├── Front+Back v1/          # Android Mobile App (Kotlin)
└── README.md             # This file
```

---

## 🚀 Project Evolution

### Front+Back v1: Flask Foundation
**Python, Flask, Vanilla JavaScript, Custom CSS**
- Basic artist search with Artsy API integration
- Custom responsive design without frameworks
- AJAX functionality with loading states
- Server-side API proxy for security

### Front+Back v2: Angular Full-Stack
**Angular, TypeScript, Bootstrap, Node.js, MongoDB**
- Modern frontend framework with responsive Bootstrap design
- Complete authentication system (JWT + cookies)
- User favorites with persistent storage
- Advanced features: similar artists, artwork galleries

### Front(Android) v2: Android Mobile
**Kotlin, Jetpack Compose, Material Design 3**
- Native Android app with modern UI patterns
- Persistent login with cookie management
- Tab-based navigation and real-time search
- Mobile-optimized user experience

---

## 🔧 Technology Progression

| Assignment | Frontend | Backend | Database | Deployment |
|------------|----------|---------|----------|------------|
| **Front+Back v1** | Vanilla JS + Custom CSS | Python Flask | None | Google Cloud |
| **Front+Back v2** | Angular + Bootstrap | Node.js + Express | MongoDB Atlas | Google Cloud |
| **Front(Android) v2** | Android (Kotlin + Compose) | Node.js (reused) | MongoDB Atlas | Emulator |

---

## 📌 Core Features (All Projects)

### 🔹 Artist Search & Discovery
- **Real-time Search**: Dynamic artist search with Artsy API
- **Artist Details**: Biography, nationality, birth/death dates
- **Artwork Galleries**: Browse artists' complete artwork collections
- **Visual Design**: Consistent card-based layouts across platforms

### 🔹 Authentication (Assignments 3 & 4)
- **User Registration**: Form validation with secure password hashing
- **Session Management**: JWT tokens with HTTP-only cookies
- **Persistent Login**: State maintained across app restarts
- **Profile Management**: Gravatar integration and account deletion

### 🔹 Favorites System (Assignments 3 & 4)
- **Add/Remove Artists**: Toggle favorite status with instant feedback
- **Persistent Storage**: MongoDB Atlas for cross-device synchronization
- **Newest-First Ordering**: Timestamped favorites with relative time display
- **Similar Artists**: Discover related artists (authenticated users only)

---

## 🛠️ Tech Stack Highlights

### Shared Components
- **Artsy API**: Artist search, details, artworks, categories
- **Security**: Backend API proxy to protect credentials
- **Responsive Design**: Mobile-first approach across all platforms
- **Error Handling**: Graceful fallbacks for missing data

### Progressive Enhancement
- **Front+Back v1**: Foundation with vanilla technologies
- **Front+Back v2**: Modern web frameworks and full authentication
- **Front(Android) v2**: Native mobile experience with advanced UI patterns

---

## 🎯 Learning Outcomes

This portfolio demonstrates proficiency in:

### Web Development
- **Frontend**: Vanilla JS → Angular → Android (Kotlin)
- **Backend**: Python Flask → Node.js Express
- **Styling**: Custom CSS → Bootstrap → Material Design 3
- **State Management**: DOM manipulation → RxJS → Jetpack Compose

### Full-Stack Skills
- **API Integration**: RESTful services with proper authentication
- **Database**: NoSQL with MongoDB Atlas
- **Authentication**: JWT tokens, session management, password security
- **Deployment**: Google Cloud Platform hosting

### Mobile Development
- **Native Android**: Kotlin with Jetpack Compose
- **Modern Patterns**: MVVM architecture, coroutines, Material Design
- **Performance**: Efficient image loading, lazy rendering, memory management

---

## 🚀 Quick Start

### Front+Back v1 (Flask)
```bash
cd assignment-2
pip install Flask requests
python app.py
```

### Front+Back v2 (Angular + Node.js)
```bash
# Backend
cd assignment-3/backend
npm install && npm start

# Frontend
cd assignment-3/frontend
npm install && ng serve
```

### Front(Android) v2 (Android)
```bash
cd assignment-4
# Open in Android Studio
# Use Pixel 8 Pro Emulator with API 34
```

---

## 📹 Demos

- **Front+Back v1**: [Flask Web Demo](https://youtu.be/fj7cPxLDiM8)
- **Front+Back v2**: [Angular Web Demo](https://youtu.be/fj7cPxLDiM8) | [Mobile Demo](https://www.youtube.com/shorts/eb504OLXJ7I)
- **Front(Android) v2**: [Android Demo](https://www.youtube.com/watch?v=Si0QBggemRA)

---

## 🎓 Academic Context

**Course**: USC CSCI 571 – Web Technologies  
**Instructor**: Marco Papa  
**Semester**: Spring 2025

This repository showcases the complete learning journey from basic web development to advanced full-stack and mobile applications, demonstrating progressive skill building in modern software development.

---

## ✨ Credits

- **APIs**: [Artsy](https://www.artsy.net/), [Gravatar](https://gravatar.com/)
- **Cloud Services**: Google Cloud Platform, MongoDB Atlas
- **Development**: USC CSCI 571 Web Technologies Course
