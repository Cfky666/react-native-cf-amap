// CfAmap.h

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <AMapSearchKit/AMapSearchKit.h>
#import <MJExtension/MJExtension.h>

@interface CfAmap : RCTEventEmitter <RCTBridgeModule,AMapSearchDelegate>

@end
