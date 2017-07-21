# ConditionerView


### Screenshots
<p align="center">
    <img src="D:\MProjects\Android\ConditionerView\screenshots\device-2017-07-22-001408.png">
</p>

### Usage

Add dependencies to the project build.gradle file: 
```groovy
compile 'com.yoyun.conditionerview:conditionerview:1.0.0'
```

layout file:
```xml
<com.yoyun.conditionerview.ConditionerView
    android:id="@+id/condView1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_weight="1"
    app:bgColor="@color/cv_bgColor"
    app:layer1Color="@color/cv_layer1Color"
    app:layer2Color="@color/cv_layer2Color"
    app:layer3Color="@color/cv_layer3Color"
    app:numberColor="@color/cv_numberColor"
    app:max="10"
    app:progress="5"/>

```

### License


```
Copyright 2017 Yoyun, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

