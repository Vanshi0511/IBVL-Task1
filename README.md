# IBVL-Task1


### 📘 **Project Overview**

This Android application demonstrates secure login and transaction retrieval using API integration with [PrepStripe API](https://api.prepstripe.com/). It features:

- Secure login using email/password.
- Biometric authentication for enhanced security on subsequent launches.
- Secure token storage using `EncryptedSharedPreferences`.
- Displays transaction history from API.
- Logout functionality to clear saved tokens.
- Bonus features include:
  - 🌙 Dark Mode
  - 💾 Offline Mode using Room
  - 🔍 Search and Filter on Transactions



### ⚙️ **Setup Instructions**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Vanshi0511/IBVL-Task1.git
   cd IBVL-Task1
   ```

2. **Open in Android Studio**:
   - Open the project folder in Android Studio.
   - Let Gradle sync completely.

3. **Dependencies**:  
   Ensure you have the following libraries in your `build.gradle` (app level):

   ```gradle
   implementation 'androidx.biometric:biometric:1.2.0-alpha04'
   implementation 'androidx.security:security-crypto:1.1.0-alpha03'
   implementation 'androidx.room:room-runtime:2.6.1'
   annotationProcessor 'androidx.room:room-compiler:2.6.1'
   implementation 'androidx.recyclerview:recyclerview:1.3.1'
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
   ```

4. **Permissions**:
   Add the following to your `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   

### 📦 **APK Build Instructions**

To generate a release-ready APK:

1. Go to **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
2. Locate the APK:
   - `app/build/outputs/apk/release/app-release.apk`


### 💎 **Bonus Features Implemented**

| Feature           | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| ✅ Dark Mode      | Supports both light and dark UI using theme resources                       |
| ✅ Offline Mode   | Transactions are cached in a local Room database for offline access         |
| ✅ Search/Filter  | Toolbar-integrated `SearchView` filters transactions in real-time           |



### 📁 **Project Structure**


com.vanshika.ibvltask1
│
├── api/
│   └── ApiService.java
│   └── RetrofitClient.java
│
├── model/
│   └── LoginRequest.java
│   └── LoginResponse.java
│   └── Transaction.java
│
├── db/
│   └── AppDatabase.java
│   └── TransactionDao.java
│   └── TransactionEntity.java
│
├── ui/
│   └── LoginActivity.java
│   └── MainActivity.java
│
├── utils/
│   └── BiometricHelper.java
│   └── TokenManager.java
│
├── adapter/
│   └── TransactionAdapter.java
│
└── res/
    └── layout/
    └── values/
    └── drawable/


### 🎯 **Final Notes**

- This project uses best practices in secure storage, modern Android components, and follows MVVM-style modularity.
- Handles no-internet scenarios gracefully.
- Supports both Light and Dark themes.
