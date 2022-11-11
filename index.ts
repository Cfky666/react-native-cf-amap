// main index.js

import { NativeEventEmitter, NativeModules } from 'react-native';
import { useEffect, useState } from "react";

const { CfAmap } = NativeModules;

export default CfAmap;

export function initAMapSearch(){
  CfAmap.initAMapSearch(()=>{});
}
export function poiSearchKeyWord({keyWord='',city=''}:{keyWord:string,city:string}){
  CfAmap.poiSearchKeyWord(keyWord,city)
}

export function useGaodeEmitter(){
  const [poiSearch,setPoiSearch] = useState<any>();
  useEffect(()=>{
    const eventEmitter = new NativeEventEmitter(NativeModules.ToastExample);
    const eventListener = eventEmitter.addListener('onPoiSearched', (event) => {
      console.log("onPoiSearched::",event.eventProperty) // "someValue"
      setPoiSearch(event.eventProperty)
    });
    return(()=>{
      eventListener && eventListener.remove();
    })
  },[])
  return poiSearch;
}
