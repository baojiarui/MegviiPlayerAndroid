# MegviiPlayerAndroid
Megvii播放器SDK，支持直播、点播，并可以边播放边缓存。
项目包含Demo和Library两个Module

## 集成说明

#### 如果要在现有的工程中集成播放SDK 可以通过导入MegviiPlayerLib module来集成SDK：
* 打开AndroidStudio的已有项目，将MegviiPlayerLib拷贝至项目根目录
* 依次选择File --> Project Structure --> Module --> Dependencies 点击加号，然后选择Module dependency --> MegviiPlayerLib
* 在AndroidManifest.xml中增加如下Activity配置：

```xml
<activity
            android:name="com.megvii.player.play.PlayActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.AppCompat.Translucent" />
```

## 如何播放
只需传递一个播放链接给播放器即可，链接可以是Mp4、m3u8、rtmp等。如果是点播链接，SDK会默认边播边缓存到本地。
```java
Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(PlayActivity.INTENT_KEY_URL, url);
        startActivity(intent);
```

## 注意事项

* 可以直接导入Demo源代码查看接入方式以及使用方法
