# react-native-cf-amap

## Getting started

`$ yarn add react-native-cf-amap`

### 注意事项
1. 只支持Hook模式

## Usage
```javascript
//ios需要初始化
import {initAMapSearch} from "react-native-cf-amap";
Constants.isIos && initAMapSearch();
```


```typescript jsx
import {poiSearchKeyWord, useAMapPoiSearchEmitter} from 'react-native-cf-amap';
const poiSearch = useAMapPoiSearchEmitter();
console.log("poiSearch::",poiSearch);
useEffect(() => {
    // initAMapSearch()
    poiSearchKeyWord({keyWord: '北京', city: ''});
}, []);
```
