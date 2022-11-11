// CfAmapModule.java

package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class CfAmapModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public CfAmapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "CfAmap";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

//
//    public void sendEventToJs(String eventName,Object obj){
//        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName,obj);
//    }
//
//
//    /**
//     * 根据key值搜索地址信息
//     * @param keyWord
//     * @param city
//     */
//    @ReactMethod
//    public void poiSearchKeyWord(String keyWord,String city) {
//        PoiSearchV2.Query query = new PoiSearchV2.Query(keyWord, "", city);
//        query.setPageSize(60);// 设置每页最多返回多少条poiitem
//        query.setPageNum(0);//设置查询页码
//        PoiSearchV2 poiSearch = null;
//        try {
//            poiSearch = new PoiSearchV2(reactContext, query);
//            poiSearch.setOnPoiSearchListener(this);
//            poiSearch.searchPOIAsyn();
//        } catch (AMapException e) {
//            e.printStackTrace();
//        }
//
////        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
//    }
//
//    /**
//     * 坐标地址查询
//     */
//    @ReactMethod
//    private void searchLocation(double latitude,double longitude) {
//        GeocodeSearch mGeocoderSearch = null;
//        try {
//            mGeocoderSearch = new GeocodeSearch(reactContext);
//            LatLonPoint point = new LatLonPoint(latitude,longitude);
//            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
//            mGeocoderSearch.getFromLocationAsyn(query);
//        } catch (AMapException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public void onPoiSearched(PoiResultV2 poiResultV2, int i) {
//        if (i == 1000) {
//            ArrayList<PoiItemV2> pois = poiResultV2.getPois();
//            Log.e(TAG,"onPoiSearched:"+pois.toString());
//            if (pois.size() > 0) {
//                sendEventToJs("onPoiSearched", pois.toString());
//
//            } else {
//
//            }
//        } else {
//            Toast.makeText( reactContext, "获取地址信息失败(" + i + ")",Toast.LENGTH_SHORT);
//        }
//    }
//
//    @Override
//    public void onPoiItemSearched(PoiItemV2 poiItemV2, int i) {
//        Log.e(TAG,"onPoiItemSearched:"+i);
//    }
//
//    @Override
//    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//        Log.e(TAG,"onRegeocodeSearched:"+i);
//
//    }
//
//    @Override
//    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//        Log.e(TAG,"onGeocodeSearched:"+i);
//    }

}
