// CfAmap.m

#import "CfAmap.h"

@interface CfAmap ()
@property(nonatomic,strong)AMapSearchAPI *search;
@end

@implementation CfAmap

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some actually useful functionality
    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}



RCT_EXPORT_METHOD(initAMapSearch:(RCTPromiseResolveBlock)resolve)
{
    NSLog(@"initAMapSearch");
    self.search = [[AMapSearchAPI alloc] init];
    self.search.delegate = self;
}

RCT_EXPORT_METHOD(poiSearchKeyWord:(NSString *)keyWord city:(nonnull NSString *)city)
{
    
    NSLog(@"poiSearchKeyWord");
    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];
        
    request.keywords        = keyWord;
    request.city                = city;
    request.types               = @"";
    [self.search AMapPOIKeywordsSearch:request];

    /*  搜索SDK 3.2.0 中新增加的功能，只搜索本城市的POI。*/
//    request.cityLimit           = YES;
//    request.requireSubPOIs      = YES;
 //    request.requireExtension    = YES;
}


/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    NSLog(@"onPOISearchDone%@",response);

    if (response.pois.count == 0)
    {
        return;
    }
//    sendEventToJs("onPoiSearched", pois.toString());
//    NSString *a =@"name";
//    [self sendEventWithName:@"onPoiSearched" body:@{@"poiSearched": @"a"}];
//    NSArray *dictArray = [AMapPOI mj_keyValuesArrayWithObjectArray : response.pois];

    [self sendEventWithName:@"onPoiSearched" body: response.pois];


    //解析response获取POI信息，具体解析见 Demo
}


- (NSArray<NSString *> *)supportedEvents{
    return @[@"onPOISearchDone"];
}








@end
