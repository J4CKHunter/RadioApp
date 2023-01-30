# RadioApp

## Simple Radio Player for Elderly People

This application is specially designed to work with Android 5.1.1 to run on Samsung Galaxy J5. 

My grandmother is 86 years old right now, and she does not know how to use a smartphone. So we designed a simple radio app that sends a permanent notification. 

With this simple approach, she only has to wake the phone up by pressing the phone's main button. And then she can easily open and close the radio by pressing the big red power button, traverse through radio channels by pressing blue buttons, and adjust the volume by pressing the yellow buttons.

### Dependencies

- exoplayer
- firebase
- coroutines
- livedata

### Project Structure
In this application, Broadcast Receivers were utilized to facilitate communication with push notifications. The MVVM design pattern was adopted as the architectural approach, with the implementation of ViewModel ensuring that the application can run in the background and data is retained even when the screen is turned off or rotated. Communication with the Firebase Real-Time Database is executed asynchronously within a coroutine scope.

### Screenshots

#### Permanent Notifications
<img src="https://github.com/aliatillaydemir/RadioApp/blob/master/Screenshots/1.png" width=30% height=70%>&ensp;&ensp;&ensp;
<img src="https://github.com/aliatillaydemir/RadioApp/blob/master/Screenshots/2.png" width=30% height=70%>&ensp;&ensp;&ensp;

#### Main app
<img src="https://github.com/aliatillaydemir/RadioApp/blob/master/Screenshots/3.png" width=30% height=70%>&ensp;&ensp;&ensp;
<img src="https://github.com/aliatillaydemir/RadioApp/blob/master/Screenshots/4.png" width=30% height=70%>&ensp;&ensp;&ensp;

<br/>

| App | LINK | EXPLANATION |
| --- | --- | --- |
| `just for push notification` | https://github.com/aliatillaydemir/RadioApp/tree/ui_branch | The main branch, this branch is currently working fine |
| `for fullscreen push notification` | https://github.com/aliatillaydemir/RadioApp/tree/fullscreen-notification | this branch needs improvements, not finished yet
