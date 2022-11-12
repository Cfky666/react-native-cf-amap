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

export function useAMapPoiSearchEmitter(){
  const [poiSearch,setPoiSearch] = useState<any>();
  useEffect(()=>{
    const eventEmitter = new NativeEventEmitter(CfAmap);
    const eventListener = eventEmitter.addListener('onPoiSearched', (event) => {
      setPoiSearch(JSON.parse(event.result || '[]'))
    });
    return(()=>{
      eventListener && eventListener.remove();
    })
  },[])
  return poiSearch;
}
