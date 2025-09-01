# 🎨 Artist Explorer Web App  
**CSCI 571 – Spring 2025 | Full-Stack Web Development Project**

A responsive web application built with **Angular, TypeScript, Bootstrap, Node.js, Express, and Artsy API**.  
The app allows users to **search artists, view detailed information, explore artworks, and manage favorites** with **JWT authentication and persistent sessions**.

---

## 📌 Features

### 🔹 Core Functionality
- **Real-time Artist Search** – Dynamic search with AJAX calls starting from user input
- **Artist Details** – Comprehensive artist information with tabbed interface
- **Artwork Gallery** – Browse artist's artworks with category modals
- **Similar Artists** – Discover related artists (authenticated users only)
- **User Authentication** – Complete registration/login system with JWT + cookies
- **Favorites Management** – Add/remove artists with persistent storage in MongoDB Atlas

### 🔹 User Experience
- **Responsive Design** – Bootstrap-powered mobile-first responsive layout
- **Persistent Sessions** – Auth state maintained across page reloads and tabs
- **Interactive Notifications** – Stackable toast notifications for user actions
- **Smooth Navigation** – Proper routing with shareable URLs for artist details
- **Real-time Updates** – Instant feedback with spinners and state changes

---

## 🛠️ Tech Stack

### Frontend
- **Framework**: Angular (TypeScript)
- **Styling**: Bootstrap (responsive design)
- **HTTP**: Angular HttpClientModule (AJAX)
- **State Management**: RxJS Subjects for auth state
- **Routing**: Angular Router with parameter handling

### Backend
- **Runtime**: Node.js + Express
- **Database**: MongoDB Atlas (NoSQL)
- **Authentication**: JWT tokens + HTTP-only cookies
- **Security**: bcrypt password hashing + Gravatar integration
- **HTTP Client**: Axios for Artsy API requests

### APIs & Services
- **Artsy API**: Artist search, details, artworks, categories
- **Gravatar**: User profile images via SHA256 email hash
- **MongoDB Atlas**: User data and favorites storage

---

## 📲 Pages Overview

| Page | Description | Auth Required |
|------|-------------|---------------|
| Search | Search form, result cards, artist details tabs | No |
| Artist Details | Biography, artworks, similar artists (shareable URLs) | Partial |
| Login | Email/password validation, JWT cookie setup | Guest only |
| Register | Full name, email, password with bcrypt hashing | Guest only |
| Favorites | User's favorite artists with relative timestamps | Yes |

---

## 🔹 Authentication System

### Registration
- Form validation (required fields, email format)
- Password hashing with bcrypt before database storage
- Gravatar profile image URL generation (SHA256 email hash)
- JWT token creation with 1-hour expiration
- HTTP-only cookie setup for security

### Login/Session Management
- Email/password verification against hashed database values
- JWT token validation with automatic session restoration
- Persistent auth state across page reloads and browser tabs
- Profile dropdown with logout and delete account options

### Protected Features
- **Similar Artists tab** (Artist Details page)
- **Star buttons** on artist cards for favorites management
- **Favorites page** with newest-first ordering
- **Profile dropdown** with user avatar and management options

---

## 🚀 How to Run

### Prerequisites
```bash
# Node.js and npm
node --version  # v18+ recommended
npm --version

# Angular CLI
npm install -g @angular/cli
```

### Backend Setup
```bash
# Install dependencies
cd backend
npm install

# Configure MongoDB Atlas connection
# Add your MongoDB connection string to environment variables

# Start backend server
npm start
```

### Frontend Setup
```bash
# Install dependencies
cd frontend
npm install

# Configure proxy for local development (proxy.conf.json)
# Start Angular development server
ng serve
```

### Deployment
- **Backend**: Deployed on Google Cloud Platform
- **Database**: MongoDB Atlas cloud instance
- **Frontend**: Served by Node.js backend with static file serving

---

## 🔧 Key Implementation Details

### API Integration
- **5 Artsy Endpoints**: Authentication, Search, Artists, Artworks, Genes (categories)
- **Secure Token Handling**: X-XAPP-Token managed on backend only
- **Error Handling**: Graceful fallbacks for missing images and data
- **Pagination**: Uses first page results only from Artsy responses

### UI/UX Features
- **Bootstrap Components**: Cards, navs, tabs, modals, forms, spinners
- **Dynamic Search**: Real-time results with hover effects and selection states
- **Category Modals**: Detailed artwork information with Bootstrap modals
- **Responsive Breakpoints**: Mobile-optimized layout for all screen sizes

### Security & Performance
- **JWT + HTTP-only Cookies**: Secure session management
- **CORS Handling**: Backend serves both API and static files
- **Input Validation**: Client-side and server-side validation
- **Async Operations**: Non-blocking AJAX calls with loading indicators

---

## 📱 Responsive Design

The application is fully responsive and tested on:
- **Desktop**: Full feature set with sidebar navigation
- **Mobile**: Optimized layouts for iPhone 14 Pro Max and similar devices
- **Tablet**: Adaptive layouts between desktop and mobile breakpoints

---

## 🎯 Learning Outcomes

This project demonstrates proficiency in:
- Full-stack web development with Angular and Node.js
- RESTful API design and integration
- JWT authentication and session management
- Responsive design with Bootstrap framework
- NoSQL database operations with MongoDB
- Cloud deployment on Google Cloud Platform
- Modern TypeScript and ES6+ JavaScript

---

## 📹 Demo

Complete demo videos showcasing all features:
- [Web Demo](https://youtu.be/fj7cPxLDiM8)
- [Mobile Demo](https://www.youtube.com/shorts/eb504OLXJ7I)

---

## ✨ Credits

- **Instructor**: Marco Papa (USC)
- **APIs**: [Artsy](https://www.artsy.net/), [Gravatar](https://gravatar.com/)
- **Course**: USC CSCI 571 – Web Technologies
