# Square Employee Directory
![Screenshot_1619266465](https://user-images.githubusercontent.com/412219/115959330-9545fc80-a4c0-11eb-8133-09405d61d80a.png) ![Screenshot_1619267825](https://user-images.githubusercontent.com/412219/115959279-4a2be980-a4c0-11eb-8486-35360d476e14.png)

This andoid application fetches a directory of employees and display in a list. It uses a static endpoing provided by the guidelines.
The main requirements was to limit fetching of HTTP data and disk caching Bitmap data. Bitmap data is not ejected and there is no http caching leaving a low memory footpprint

Since the ViewModel and coresponding repository are bound to the parent activity the results will be GC on activity destruction

Fetching and displaying this data uses standard jetpack / and the android architecture pattern MVVM serializing data with GSON. 
Using an intermediary wrappper, to handoff to the
viewmodel for easy rendering. Business logic and UI logic are stricly sepereated. 

[MVVM architecture](https://developer.android.com/jetpack/guide)

We have 4 main states for fetching data
LOADING
SUCCSSS
ERROR 
EMPTY

Running this is standard
```
JVM8
Android Compile Version 30
Build tools 30.0.3
android.enableJetifier=true
Gradle gradle-6.7.1
```

Thre is currently one fragment implementing the data from the endpoint howevever we're using a shared view model approach as it is easily extendable to other fragments
and activities. Ie list -> detail view.

UI is not totally standard, the list items are unique. 

I generally built this with phone in mind instead of tablet without knowing context of what the extension would be. If it's an app more for a merchant, the tablet might be better but as of now it wouldn't make a difference, simlply a list.

Many people prefer tablets on the merget side, that would mainly be to make it easier for payment processing, however this is a list of internal employees so building for that seems more reasonable. Maybe you need to reach a colleage on your team, or get contact info. A tablet isn't necesarry for that. Search can be better implemented on a phone.

Testing is limited to Unit testing for now, mocking servers and integration testing would take more time but definitely necessary. 

What I tried to keep in mind is just the overall struct of not having a slow boot, the Activities and Fragments, Views as "dumb". The business logic is mostly in the view models but much can be offloaded into the repo layer as well. Also it looks nice :) 
