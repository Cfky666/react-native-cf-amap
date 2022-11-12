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



RCT_EXPORT_METHOD(initAMapSearch:(RCTResponseSenderBlock)callback)
{
    NSLog(@"initAMapSearch");
    self.search = [[AMapSearchAPI alloc] init];
    self.search.delegate = self;
    callback(@[[NSNull null], @"success"]);
}

RCT_EXPORT_METHOD(poiSearchKeyWord:(NSString *)keyWord city:(nonnull NSString *)city)
{
   
    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];
        
    request.keywords        = keyWord;
    request.city                = city;
    request.types               = @"";
//    NSLog(@"poiSearchKeyWord%@",request);

    [self.search AMapPOIKeywordsSearch:request];

}


/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    
//    NSLog(@"onPOISearchDone%@",response.pois);

    if (response.pois.count == 0)
    {
        NSArray *result = [NSArray array];
        [self sendEventWithName:@"onPoiSearched" body: @{@"result":result}];
        return;
    }

    NSArray *result = [AMapPOI mj_keyValuesArrayWithObjectArray:response.pois];
    [self sendEventWithName:@"onPoiSearched" body: @{@"result":result}];

}

/**
 支持的发送事件
 */
- (NSArray<NSString *> *)supportedEvents{
    return @[@"onPoiSearched"];
}








@end
