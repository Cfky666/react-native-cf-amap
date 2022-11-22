// main index.js

import {NativeEventEmitter, NativeModules, Platform} from 'react-native';
import { useEffect, useState } from "react";

const { CfAmap } = NativeModules;

export default CfAmap;

export function initAMapSearch(){
  CfAmap.initAMapSearch((res:any)=>{
    console.log('result:',res)
  });
}

/**
 * 关键字搜索
 * @param keyWord
 * @param city
 */
export function poiSearchKeyWord({keyWord='',city=''}:{keyWord:string,city:string}){
  CfAmap.poiSearchKeyWord(keyWord,city)
}

/**
 * 周边搜索
 * @param latitude
 * @param longitude
 */
export function poiSearchBound({latitude,longitude}:{latitude:number;longitude:number;}){
  CfAmap.poiSearchBound(latitude,longitude)
}

export function usePoiSearchBoundEmitter() {
  const [poiSearch,setPoiSearch] = useState<any>();
  useEffect(()=>{
    const eventEmitter = new NativeEventEmitter(CfAmap);
    const eventListener = eventEmitter.addListener('onPoiSearchBound', (event) => {
      if(Platform.OS === 'ios'){
        setPoiSearch(event.result || []);
      }else {
        setPoiSearch(JSON.parse(event.result || '[]'));
      }
    });
    return(()=>{
      eventListener && eventListener.remove();
    })
  },[])
  return poiSearch;
}

/**
 * 监听
 */
export function useAMapPoiSearchEmitter(){
  const [poiSearch,setPoiSearch] = useState<any>();
  useEffect(()=>{
    const eventEmitter = new NativeEventEmitter(CfAmap);
    const eventListener = eventEmitter.addListener('onPoiSearched', (event) => {
      if(Platform.OS === 'ios'){
        setPoiSearch(event.result || []);
      }else {
        setPoiSearch(JSON.parse(event.result || '[]'));
      }
    });
    return(()=>{
      eventListener && eventListener.remove();
    })
  },[])
  return poiSearch;
}
