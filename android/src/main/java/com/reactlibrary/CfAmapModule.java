// CfAmapModule.java

package com.reactlibrary;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItemV2;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResultV2;
import com.amap.api.services.poisearch.PoiSearchV2;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CfAmapModule extends ReactContextBaseJavaModule implements PoiSearchV2.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener {

    private final ReactApplicationContext reactContext;
    private String TAG = "CfAmapModule";
    private String TypeFlag = "";

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

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    /**
     * 根据key值搜索地址信息
     *
     * @param keyWord
     * @param city
     */
    @ReactMethod
    public void poiSearchKeyWord(String keyWord, String city) {
        TypeFlag = "poiSearchKeyWord";
        PoiSearchV2.Query query = new PoiSearchV2.Query(keyWord, "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearchV2 poiSearch = null;
        try {
            poiSearch = new PoiSearchV2(reactContext, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }


    @ReactMethod
    public void poiSearchBound(double latitude, double longitude) {
        TypeFlag = "poiSearchBound";
        PoiSearchV2.Query query = new PoiSearchV2.Query("", "");
//        query.setPageSize(20);// 设置每页最多返回多少条poiitem
//        query.setPageNum(0);//设置查询页码
        PoiSearchV2 poiSearch = null;
        try {
            poiSearch = new PoiSearchV2(reactContext, query);
            poiSearch.setBound(new PoiSearchV2.SearchBound(new LatLonPoint(latitude,longitude), 5000));//设置周边搜索的中心点以及半径
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 坐标地址查询
     */
    @ReactMethod
    private void searchLocation(double latitude, double longitude) {
        GeocodeSearch mGeocoderSearch = null;
        try {
            mGeocoderSearch = new GeocodeSearch(reactContext);
            LatLonPoint point = new LatLonPoint(latitude, longitude);
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
            mGeocoderSearch.getFromLocationAsyn(query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过经纬度逆地理编码得到位置
     * @param latitude
     * @param longitude
     */
    @ReactMethod
    private void getAddressByLatlng(double latitude, double longitude) {
        try {
            GeocodeSearch geocodeSearch = new GeocodeSearch(reactContext);
            geocodeSearch.setOnGeocodeSearchListener(this);
            LatLonPoint latLng = new LatLonPoint(latitude, longitude);
            //第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLng, 200, GeocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(query);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }
    }


    private String object2String(Parcelable stu) {
        // 1.序列化
        Parcel p = Parcel.obtain();
        stu.writeToParcel(p, 0);
        byte[] bytes = p.marshall();
        p.recycle();

        // 2.编码
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    /**
     * poi结果返回
     *
     * @param poiResultV2
     * @param i
     */
    @Override
    public void onPoiSearched(PoiResultV2 poiResultV2, int i) {
//        Log.e(TAG,"onPoiSearched:"+i);
        if (i == 1000) {
            ArrayList<PoiItemV2> pois = poiResultV2.getPois();
            if (pois.size() > 0) {
                Log.e(TAG, "onPoiSearched:" + pois);
                WritableMap params = Arguments.createMap();
                params.putString("result", new Gson().toJson(pois));
                if (TextUtils.equals(TypeFlag, "poiSearchKeyWord")) {
                    sendEvent(reactContext, "onPoiSearched", params);
                } else if (TextUtils.equals(TypeFlag, "poiSearchBound")) {
                    sendEvent(reactContext, "onPoiSearchBound", params);
                }

            } else {
                WritableMap params = Arguments.createMap();
                params.putString("result", "[]");
                if (TextUtils.equals(TypeFlag, "poiSearchKeyWord")) {
                    sendEvent(reactContext, "onPoiSearched", params);
                } else if (TextUtils.equals(TypeFlag, "poiSearchBound")) {
                    sendEvent(reactContext, "onPoiSearchBound", params);
                }
            }
        } else {
            Toast.makeText(reactContext, "获取地址信息失败(" + i + ")", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItemV2 poiItemV2, int i) {
        Log.e(TAG, "onPoiItemSearched:" + i);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//        Log.e("获取地址开始===", i + "=====" + regeocodeResult.getRegeocodeAddress().getFormatAddress());
        if (i == 1000) {
            Log.e(TAG, "地址回掉::" + regeocodeResult.getRegeocodeAddress().getFormatAddress());
            WritableMap params = Arguments.createMap();
            params.putString("result", regeocodeResult.getRegeocodeAddress().getFormatAddress());
            sendEvent(reactContext, "onReGeocodeSearched", params);
        } else {
            Log.e(TAG, "地址查询失败::");
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Log.e(TAG, "onGeocodeSearched:" + i);
    }

    @ReactMethod
    public void addListener(String eventName) {
        // Set up any upstream listeners or background tasks as necessary
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        // Remove upstream listeners, stop unnecessary background tasks
    }

}
