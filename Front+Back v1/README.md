# 🎨 Artist Search Web App  
**CSCI 571 – Spring 2025 | Server-Side + Basic Front-end Scripting Project**

A web application built with **Python, Flask, HTML, CSS, JavaScript, and Artsy API**.  
The app allows users to **search artists and view detailed artist information** with **responsive design and AJAX functionality**.

---

## 📌 Features

### 🔹 Core Functionality
- **Artist Search** – Search artists using Artsy API with real-time AJAX calls
- **Artist Cards** – Visual result cards with images and artist names
- **Artist Details** – Comprehensive artist information (biography, nationality, dates)
- **Interactive UI** – Hover effects, loading states, and smooth transitions
- **Responsive Design** – Mobile-friendly layout without external CSS frameworks

### 🔹 User Experience
- **Search Bar** – Custom-styled search with magnifying glass and clear icons
- **Loading States** – Animated GIF indicators during API calls
- **Error Handling** – Graceful handling of empty results and missing data
- **Visual Feedback** – Card selection states and hover animations
- **Fallback Images** – Artsy logo for artists without profile images

---

## 🛠️ Tech Stack

### Backend
- **Language**: Python
- **Framework**: Flask
- **HTTP Client**: Python requests library
- **Deployment**: Google Cloud Platform
- **API Integration**: Artsy REST API (3 endpoints)

### Frontend
- **Languages**: HTML5, CSS3, Vanilla JavaScript
- **AJAX**: Fetch API / XMLHttpRequest
- **Styling**: Custom CSS (no external frameworks)
- **Icons**: Custom magnifying glass and cross icons
- **Images**: Artsy logo, loading GIF animations

### APIs & Services
- **Artsy API**: Authentication, Search, Artists endpoints
- **Google Cloud**: Backend deployment and hosting

---

## 📲 Features Overview

| Feature | Description |
|---------|-------------|
| Search Bar | Custom-styled input with icons, focus states, validation |
| Artist Cards | Circular images, blue backgrounds, hover effects |
| Artist Details | Name, birth/death dates, nationality, biography |
| Loading States | Animated GIFs during API calls |
| Error Handling | Empty results, missing images, validation alerts |
| Responsive Design | Mobile-optimized without external CSS frameworks |

---

## 🔹 API Integration

### Artsy Endpoints Used
1. **Authentication** (`POST /api/tokens/xapp_token`)
   - Retrieves X-XAPP-Token for API access
   - Manages token expiration (1-week validity)

2. **Search** (`GET /api/search`)
   - Searches artists by name with query parameter
   - Filters results to artist type only
   - Returns max 10 results per request

3. **Artists** (`GET /api/artists/{id}`)
   - Fetches detailed artist information by ID
   - Returns biography, nationality, birth/death dates

### Security & Architecture
- **Backend Proxy**: All Artsy API calls routed through Flask backend
- **Token Management**: X-XAPP-Token handled securely on server-side
- **No Direct API Calls**: Frontend never directly accesses Artsy APIs
- **GET Requests Only**: All frontend-backend communication via HTTP GET

---

## 🚀 How to Run

### Prerequisites
```bash
# Python 3.8+ and pip
python --version
pip --version

# Flask
pip install Flask requests
```

### Local Development
```bash
# Clone repository
git clone https://github.com/Nikelroid/artist-explorer-app.git
cd artist-search-flask

# Set up environment variables
export CLIENT_ID="your_artsy_client_id"
export CLIENT_SECRET="your_artsy_client_secret"

# Run Flask application
python app.py
```

### Deployment (Google Cloud)
```bash
# Deploy to Google Cloud Platform
gcloud app deploy

# View deployed application
gcloud app browse
```

---

## 🔧 Implementation Highlights

### Custom Styling
- **No Bootstrap**: Pure CSS implementation for responsive design
- **Custom Icons**: Magnifying glass and cross icons with opacity effects
- **Color Scheme**: Blue theme (#205375, #112B3C) with orange focus states
- **Circular Images**: Border-radius styling for artist profile pictures

### JavaScript Features
- **Vanilla JavaScript**: No external JS frameworks or libraries
- **AJAX Calls**: Fetch API for backend communication
- **DOM Manipulation**: Dynamic content updates and state management
- **Event Handling**: Search triggers, hover effects, card selection

### Backend Architecture
- **Flask Routes**: Separate endpoints for search and artist details
- **Error Handling**: Proper HTTP status codes and error messages
- **Static File Serving**: Frontend files served by Flask
- **API Proxy**: Secure Artsy API integration without exposing credentials

---

## 🎯 Key Use Cases

### Search Flow
1. User enters artist name in search bar
2. Frontend validates input and sends AJAX request to Flask backend
3. Backend forwards request to Artsy Search API
4. Results displayed as interactive artist cards
5. User clicks card to view detailed information

### Detail View
1. Frontend sends artist ID to backend via AJAX
2. Backend fetches artist details from Artsy Artists API
3. Detailed information displayed below search results
4. Card remains highlighted to show active selection

### Error Scenarios
- Empty search input triggers browser validation alert
- No search results shows "No results found" message
- Missing artist images replaced with Artsy logo
- Missing biography/nationality fields handled gracefully

---

## 📹 Demo

Reference demo video: [Artist Search Demo](https://youtu.be/fj7cPxLDiM8)

---

## ✨ Credits

- **Instructor**: Marco Papa (USC)
- **APIs**: [Artsy](https://www.artsy.net/)
- **Course**: USC CSCI 571 – Web Technologies
