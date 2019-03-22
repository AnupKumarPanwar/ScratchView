# ScratchView

This repo is an extension to https://github.com/sharish/ScratchView

  
Demo Screen
------    

|     ScratchImageView                |        ScratchTextView              | 
| ----------------------------  | ----------------------------- | 
| ![ScratchImageView][scratch_image] | ![ScratchTextView][scratch_text]   |

### Useful Methods

Both the views have following three methods which are useful to reveal or determine whether revealed and listener during revealing the hidden text/image.

* ```isRevealed()``` - tells whether the text/image has been revealed.
* ```reveal()``` - reveals the image/text if not revealed yet.
* ```setRevealListener(IRevealListener)``` - a callback listener interface which gets called back when user reveals the text/image
through onReveal() method.

Usage
--------

### ScratchImageView

##### XML

```xml
<com.cooltechworks.views.ScratchImageView
  android:id="@+id/sample_image"
  android:layout_width="300dp"
  android:layout_height="300dp"
  android:background="@android:color/white"
  android:src="@drawable/img_sample2"
  app:overlay_image="@drawable/scratch_card"
  app:tile_mode="CLAMP"
  app:overlay_width="300dp"
  app:overlay_height="300dp"
  />

```

##### JAVA

```java
ScratchImageView scratchImageView = new ScratchImageView(this);

scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
    @Override
    public void onRevealed(ScratchImageView tv) {
        // on reveal
    }

    @Override
    public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
        // on image percent reveal
    }
});
```

### ScratchTextView

##### XML

```xml
<com.cooltechworks.views.ScratchTextView
  android:layout_width="300dp"
  android:layout_height="300dp"
  android:gravity="center|end"
  android:text="@string/flat_200"
  android:textSize="15sp"
  android:textStyle="bold"
  app:overlay_image="@drawable/scratch_card"
  app:tile_mode="CLAMP"
  app:overlay_width="300dp"
  app:overlay_height="300dp"/>

```

##### JAVA

```java
ScratchTextView scratchTextView = new ScratchTextView(this);

scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
    @Override
    public void onRevealed(ScratchTextView tv) {
        //on reveal
    }


    @Override
    public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
        // on text percent reveal
    }
});
```


Attributes
--------
|     Attribute                |        Description              |          Values               | 
| ----------------------------  | ----------------------------- |  ----------------------------- |
| `app:overlay_image` | The pattern or image to cover the image/text.  | @drawable/scratch_card |
| `app:tile_mode` | https://developer.android.com/reference/android/graphics/Shader.TileMode | CLAMP / MIRROR / REPEAT|
| `app:overlay_width` | Width of the overlay pattern | 300dp |
| `app:overlay_height` | Height of the overlay pattern | 300dp |


Adding to your project
------------------------

- Add the following configuration in your build.gradle file.

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.AnupKumarPanwar:ScratchView:1.0'
}
```

Developed By
------------

* Harish Sridharan - <harish.sridhar@gmail.com>


Customised By
------------

* Anup Kumar Panwar - <1anuppanwar@gmail.com>



[scratch_image]:https://raw.githubusercontent.com/cooltechworks/ScratchView/2ec97c9a539d5976b68bf62ec07df8c727d72be2/screenshots/scratch_image_view_demo.gif
[scratch_text]:https://raw.githubusercontent.com/cooltechworks/ScratchView/master/screenshots/scratch_text_view_demo.gif



