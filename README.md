# ScratchView

This repo is build over https://github.com/sharish/ScratchView

  
Demo Screen
------    

![](https://raw.githubusercontent.com/AnupKumarPanwar/ScratchView/master/Screenshots/ScratchView.gif)


### Useful Methods

* ```isRevealed()``` - tells whether the view has been revealed.
* ```reveal()``` - reveals the view if not revealed yet.
* ```mask()``` - masks the view again and removes the existing scratches.
* ```setRevealListener(IRevealListener)``` - a callback listener interface which gets called back when user reveals the view
through onReveal() method.


Usage
--------

ScratchView acts as an overlay over other views. Scratching the view makes it transparent so that the views below it are visible. Wrap all the layers in a RelativeLayout and put ScratchView above it.


##### XML

```xml
<com.anupkumarpanwar.scratchview.ScratchView
  android:id="@+id/scratch_view"
  android:layout_width="300dp"
  android:layout_height="300dp"
  app:overlay_image="@drawable/scratch_card"
  app:tile_mode="CLAMP"
  app:overlay_width="300dp"
  app:overlay_height="300dp"
  />

```

##### JAVA

```java
ScratchView scratchView = findViewById(R.id.scratch_view);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                Toast.makeText(getApplicationContext(), "Reveled", Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
                }
            }
        });
```


Example
--------

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    tools:context=".MainActivity">

    <ImageView
        android:gravity="center"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:src="@drawable/anup"/>

    <com.anupkumarpanwar.scratchview.ScratchView
        android:id="@+id/scratch_view"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:overlay_image="@drawable/scratch_card"
        app:tile_mode="CLAMP"
        app:overlay_width="300dp"
        app:overlay_height="300dp"
        />

</RelativeLayout>

```

Attributes
--------
|     Attribute                |        Description              |          Values               | 
| ----------------------------  | ----------------------------- |  ----------------------------- |
| `app:overlay_image` | The pattern or image to cover the view.  | @drawable/scratch_card |
| `app:tile_mode` | https://developer.android.com/reference/android/graphics/Shader.TileMode | CLAMP / MIRROR / REPEAT|
| `app:overlay_width` | Width of the overlay pattern | 300dp |
| `app:overlay_height` | Height of the overlay pattern | 300dp |


Installation
------------------------

- Add the following configuration in your build.gradle file.

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.AnupKumarPanwar:ScratchView:1.2'
}
```

Developed By
------------

* Anup Kumar Panwar - <1anuppanwar@gmail.com>
