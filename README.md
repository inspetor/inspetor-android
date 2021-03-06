
<p>
 <img src="https://files.lgtcdn.net/images/legiti_logo_black.png" width="200" height="55" alt="Legiti Logo">
</p>

# Legiti Antifraud
Legiti Antifraud SDK for Android.

[ ![Download](https://api.bintray.com/packages/legiti/legiti-android/legiti/images/download.svg) ](https://bintray.com/legiti/legiti-android/legiti/_latestVersion)

## Description
Legiti is a product developed to help your company avoid fraudulent transactions. This README file should help you to integrate the Legiti Android library into your product. 

P.S.: This library was made in Kotlin and all of the code you'll see here is in Kotlin as well.

## Demo
If you want to see a very simple integration of the library in action you can clone the [legiti demo app](https://github.com/legiti/legiti-android-demo-app). There you find an implementation on how to setup the library and trigger all tracking actions. Apart from that, you find the best practices when using our library.

## How to use
The Legiti Android Library can be installed through [Jcenter](https://jcenter.bintray.com/). To install all you have to do is follow this steps:

1. Add this line into your root `build.gradle` (**Project**) at the end of repositories (if it's not there yet):
```
allprojects {
  repositories {
    ...
    jcenter()
  }
}
```
2. Add this dependency to your application `build.gradle` (**Module.app**). Remember to change the version to the last one:
```
dependencies {
  ...
  implementation 'com.legiti:legiti:[version]@aar', { transitive = true }
  (e.g. 'com.legiti:legiti:1.0.0@aar')
}
```

The **transitive** statement says that the library will be imported with its dependencies. It's really important, so ***don't forget !***

## API Docs
You can find more in-depth documentation about our frontend libraries and integrations in general [here](https://docs.legiti.com).

### Library setup
In order to properly relay information to Legiti's processing pipeline, you'll need to provide your customer-specific authentication credential (your authToken, which is provided to you by Legiti)

**P.S:** Remember to use the *sandbox* `authToken` when you are not in production

With these, you can instantiate the Legiti tracking instance. Our integration library instantiates a singleton instance to prevent multiple trackers from being instantiated, which could otherwise result in duplicate or inconsistent data being relayed to Legiti. Apart from that, the singleton will let you configure the library only once.

The singleton instance is instantiated as follows:

```
try {
    Legiti.sharedInstance().setup(authToken="authToken")
} catch (ex: Exception) {
    when (ex) {
        is InvalidCredentials -> { print("Error: $ex") }
        is ContextNotSetup -> { Legiti.sharedInstance().setContext(context=applicationContext) }
    }
}
```

Be advised, that this function can throw two types of exceptions:

1. `InvalidCredentials` -> If you pass an invalid appId or/and trackerName (empty strings or not in the format required).
2. `ContextNotSetup` -> The Legiti Android Library will try to get the `ApplicationContext` automatically on setup, but if this does not work you will need to manually pass the context to the library by using the `setContext` function

We **strongly** recommend you instantiate the Legiti Library in your application `onCreate` function, since this way you will configure the library as soon as the app loads enabling you to call the library functions.

All the access to the Legiti functions is made by calling the `Legiti.sharedInstance()`.

The parameters passed are the following, in order:

Parameter | Required | Type | Description
--------- | -------- | ---- | -----------
authToken       | Yes | String  | A unique identifier that the Legiti Team will provide to you

P.S: always remember to import the library using the `import com.legiti.Legiti`

### Library Calls
If you've already read the [Legiti docs](https://docs.legiti.com), you should be aware of all of Legiti requests and collection functions.

Here we will show you some details to be aware of if you are calling the Legiti tracking functions.

All of out *track functions* can throw exceptions, but the only exception they will through is if you forget to configure the Legiti Library before calling one of them. Because of that, the Legiti class has a function called `isConfigured()` that returns a boolean saying if you have configured or not the Legiti Library. We recommend that when you call any of our tracking functions you check if the Legiti Library is configured. Here is an example on how to do that:

```
if (Legiti.sharedInstance().isConfigured()) {
    Legiti.sharedInstance().trackUserCreation(accountId="123")
}
```

#### TrackScreenView
Different from the Legiti Javascript Library the track of user pageviews (screenview) is not done automatically. You need to add the function `trackPageView` on every new page.
We **strongly recommend** that you add the functions inside the `onCreate` function of every file associated with a `contentView` in your app, since this way we can track the pageview/screenview action as soon as it happens. You can see an example of this implementation bellow:

```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (Legiti.sharedInstance().isConfigured()) {
        Legiti.sharedInstance().trackPageView(pageTitle="login-page")
    }
}
```

### User Location
The Legiti Android Library can use the user location to help us provide more accurate results, but it will **never** ask for it. If your app already has access to user location the library will automatically capture it, otherwise, it won't send the location to us.

### Models
If you are coming from one of our backend libraries you will notice that we do not use models (e.g. Account, Sale) in our frontend libraries. Here you just need to send us the id of the model (e.g. sale ID, account ID).

## More Information
For more info, you should check the [Legiti docs](https://docs.legiti.com)
