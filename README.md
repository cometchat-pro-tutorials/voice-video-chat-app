# How to Build a Voice & Video Chat App in Java

Read the full tutorial here: [**>> How to Build a Voice & Video Chat App in Java**](https://www.cometchat.com/tutorials/#)

This example shows How to Build a Voice & Video Chat App in Java:

[Live Demo](https://www.dropbox.com/preview/hieptl/comet-chat-todo/voice-video-chat-app-cometchat/voice-video-chat-app.mov?context=browse&role=personal)
<center><figcaption>How to Build a Voice & Video Chat App in Java</figcaption></center>

## Technology

This demo uses:

- Android
- CometChat Android UI Kit
- Firebase

## Running the demo

To run the demo, you need to have Android Studio installed on your computer and follow these steps:

1. [Head to CometChat Pro and create an account](https://app.cometchat.com/signup)
2. From the [dashboard](https://app.cometchat.com/apps), add a new app called **"voice-video-chat-app"**
3. Select this newly added app from the list.
4. From the Quick Start copy the **APP_ID, APP_REGION and AUTH_KEY**. These will be used later.
5. Also copy the **REST_API_KEY** from the API & Auth Key tab.
6. Navigate to the Users tab, and delete all the default users and groups leaving it clean **(very important)**.
7. Download the repository [here](https://github.com/hieptl/voice-video-chat-app/archive/main.zip) or by running `git clone https://github.com/hieptl/voice-video-chat-app.git` and open it in a code editor.
8. [Head to Firebase and create a new project](https://console.firebase.google.com)
9. Create a file called Constants.java in the package folder of your project.
10. Import and inject your secret keys in the .env file containing your CometChat and Firebase in this manner.

```js
String COMETCHAT_APP_ID = xxx-xxx-xxx-xxx-xxx-xxx-xxx-xxx;
String COMETCHAT_REGION = xxx-xxx-xxx-xxx-xxx-xxx-xxx-xxx;
String COMETCHAT_AUTH_KEY = xxx-xxx-xxx-xxx-xxx-xxx-xxx-xxx;
String FIREBASE_REALTIME_DATABASE_URL = xxx-xxx-xxx-xxx-xxx-xxx-xxx-xxx;
String FIREBASE_EMAIL_KEY = "email"; // this is not a secreat value, it is just a constant variable that will be accessed from different places of the application.
String FIREBASE_USERS = "users"; // this is not a secreat value, it is just a constant variable that will be accessed from different places of the application.
```
    
11. Make sure to include the Constants.java file in your gitIgnore file from being exposed online.

Questions about running the demo? [Open an issue](https://github.com/hieptl/voice-video-chat-app/issues). We're here to help ✌️

## Useful links

- 🏠 [CometChat Homepage](https://app.cometchat.com/signup)
- 🚀 [Create your free account](https://app.cometchat.com/apps)
- 📚 [Documentation](https://prodocs.cometchat.com)
- 👾 [GitHub](https://www.github.com/cometchat-pro)
- 🔥 [Firebase](https://console.firebase.google.com)
- 🔷 [Android](https://developer.android.com)