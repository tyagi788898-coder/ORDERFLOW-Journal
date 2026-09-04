android {
    namespace 'com.institutional.tradingjournal'
    compileSdk 34

    defaultConfig {
        applicationId "com.institutional.tradingjournal"
        minSdk 24
        targetSdk 34
        versionCode 2
        versionName "1.0.1"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        debug {
            // Consistent debug signing across builds allows direct update without uninstall
            storeFile file("${System.getProperty('user.home')}/.android/debug.keystore")
            storePassword "android"
            keyAlias "androiddebugkey"
            keyPassword "android"
        }
    }

    buildTypes {
        debug {
            signingConfig signingConfigs.debug
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            // For continuous delivery before final store upload, debug signing keeps update working
            signingConfig signingConfigs.debug
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion '1.5.8'
    }
}
