// main index.js

import {NativeEventEmitter, NativeModules, Platform} from "react-native";
import {useEffect, useState} from "react";

const {CfAmap} = NativeModules;

export default CfAmap;

export function initAMapSearch() {
    CfAmap.initAMapSearch((res: any) => {
        console.log("result:", res);
    });
}

/**
 * 关键字搜索
 * @param keyWord
 * @param city
 */
export function poiSearchKeyWord({keyWord = "", city = ""}: { keyWord: string, city: string }) {
    CfAmap.poiSearchKeyWord(keyWord, city);
}

/**
 * 周边搜索
 * @param latitude
 * @param longitude
 */
export function poiSearchBound({latitude, longitude}: { latitude: number; longitude: number; }) {
    const isIos = Platform.OS === "ios";
    CfAmap.poiSearchBound(isIos ? latitude + "" : latitude, isIos ? longitude + "" : longitude);
}

/**
 * 逆地理编码
 * @param latitude
 * @param longitude
 */
export function getAddressByLatlng({latitude, longitude}: { latitude: number; longitude: number; }) {
    console.log("getAddressByLatlng::", latitude, longitude);
    const isIos = Platform.OS === "ios";

    // if (isIos) {
    CfAmap.getAddressByLatlng(isIos ? latitude + "" : latitude, isIos ? longitude + "" : longitude)
    // }else {
    //   CfAmap.getAddressByLatlng(isIos ? latitude + "" : latitude, isIos ? longitude + "" : longitude).then((v: string) => {
    //     console.log("v::", v);
    //   }).catch((e: any) => {
    //     console.log("e::", e);
    //   });
    // }
}

/**
 * 逆地理编码
 */
export function useReGeocodeBoundEmitter() {
    const [reGeocode, setReGeocode] = useState<any>();
    useEffect(() => {
        const eventEmitter = new NativeEventEmitter(CfAmap);
        const eventListener = eventEmitter.addListener("onReGeocodeSearched", (event) => {
            if (Platform.OS === "ios") {
                console.log("event.result::::::::", event.result)
                // setReGeocode(event.result?.formattedAddress  || "");
                setReGeocode({
                    address: event.result?.formattedAddress || "",
                    adcode: event.result?.addressComponent?.adcode ?? '',
                    province: event.result?.addressComponent?.province,
                    city: event.result?.addressComponent?.city,
                    district: event.result?.addressComponent?.district
                });
            } else {
                console.log("android event.result::::::::", event)

                setReGeocode({
                    address: event.formattedAddress || "",
                    adcode: event.adCode,
                    province: event.province,
                    city: event.city,
                    district: event.district
                });
            }
        });
        return (() => {
            eventListener && eventListener.remove();
        });
    }, []);
    return reGeocode;
}

export function usePoiSearchBoundEmitter() {
    const [poiSearch, setPoiSearch] = useState<any>();
    useEffect(() => {
        const eventEmitter = new NativeEventEmitter(CfAmap);
        const eventListener = eventEmitter.addListener("onPoiSearchBound", (event) => {
            if (Platform.OS === "ios") {
                setPoiSearch(event.result || []);
            } else {
                setPoiSearch(JSON.parse(event.result || "[]"));
            }
        });
        return (() => {
            eventListener && eventListener.remove();
        });
    }, []);
    return poiSearch;
}

/**
 * 监听
 */
export function useAMapPoiSearchEmitter() {
    const [poiSearch, setPoiSearch] = useState<any>();
    useEffect(() => {
        const eventEmitter = new NativeEventEmitter(CfAmap);
        const eventListener = eventEmitter.addListener("onPoiSearched", (event) => {
            if (Platform.OS === "ios") {
                setPoiSearch(event.result || []);
            } else {
                setPoiSearch(JSON.parse(event.result || "[]"));
            }
        });
        return (() => {
            eventListener && eventListener.remove();
        });
    }, []);
    return poiSearch;
}
