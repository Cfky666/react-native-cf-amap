// main index.js

import { NativeEventEmitter, NativeModules } from 'react-native';
import { useEffect, useState } from "react";

const { CfGaode } = NativeModules;

export default CfGaode;

export function poiSearchKeyWord({keyWord='',city=''}:{keyWord:string,city:string}){
  CfGaode.poiSearchKeyWord(keyWord,city)
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
