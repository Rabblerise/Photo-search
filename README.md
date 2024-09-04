# Laboratory 3 - Android Image Search and Display App

## Overview

This Android application allows users to search for images using the Google Custom Search API and display them within the app. Users can like or dislike images, and view the liked images in a separate layout. The app also features Google Sign-In functionality, enabling users to sign in with their Google accounts.
<div align="center">
 <img src="https://github.com/user-attachments/assets/1793a06f-7548-4e97-be41-205a8215a49a" alt="изображение" width="400"/>
</div>

## Features

- **Google Sign-In**: Users can sign in with their Google accounts to access personalized features.
- **Image Search**: Users can search for images by entering a query. The app retrieves images using the Google Custom Search API.
- **Image Display**: Images are displayed one at a time, with options to like or dislike each image.
- **Navigation**: Users can navigate between the main image display screen and a secondary screen that shows a list of liked images.
- **Image Interaction**: Users can tap on an image to open the corresponding link in a web browser.

## Setup and Installation

### Prerequisites

- Android Studio installed on your computer.
- A Google Cloud project with Custom Search API enabled.
- Google Sign-In configured in your project.

### Clone the Repository

```bash
git clone https://github.com/yourusername/laboratory3.git
cd laboratory3
```
# Configure the Project
1. Google Custom Search API:
- Replace the apiKey and cseId in the loadImages method with your own API key and search engine ID from Google Cloud.
2. Google Sign-In:
- Ensure that your google-services.json file is correctly placed in the app/ directory.
- Configure your project in the Firebase Console if needed.

# Running the App
1. Open the project in Android Studio.
2. Sync the project with Gradle files.
3. Run the app on an emulator or physical device.

# Usage
- Search Images: Enter a query in the search bar and tap the search button to fetch and display images.
- Like/Dislike Images: Use the like and dislike buttons to navigate through images and save liked images.
- View Liked Images: Navigate to the liked images screen using the designated button.
- Sign In: Sign in with your Google account to access personalized features.

# Code Overview
- MainActivity.kt: The main activity handles Google Sign-In, image search, image display, and user interactions such as liking/disliking images and navigating between layouts.
- GoogleCustomSearchApi.kt: Interface for interacting with the Google Custom Search API.
- layouts: The project contains two main layouts, one for the image search and display, and another for viewing the liked images list.
