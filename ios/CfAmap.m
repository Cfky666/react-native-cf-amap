// CfAmap.m

#import "CfAmap.h"

@interface CfAmap ()
@property(nonatomic,strong)AMapSearchAPI *search;
@end

@implementation CfAmap

static NSString *SearchType = @"";

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
    SearchType = @"poiSearchKeyWord";

    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];

    request.keywords        = keyWord;
    request.city                = city;
    request.types               = @"";
//    NSLog(@"poiSearchKeyWord%@",request);

    [self.search AMapPOIKeywordsSearch:request];

}

RCT_EXPORT_METHOD(poiSearchBound:(NSString *)latitude longitude:(nonnull NSString *)longitude)
{
    SearchType = @"searchLocation";

    float lat = [latitude floatValue];
    float lon = [longitude floatValue];

//    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];
    AMapPOIAroundSearchRequest *request = [[AMapPOIAroundSearchRequest alloc] init];
    NSLog(@"latitude :%f,longitude: %f",lat, lon);
    request.location            = [AMapGeoPoint locationWithLatitude:lat longitude:lon];
    request.keywords            = @"";
    /* 按照距离排序. */
    request.sortrule            = 0;
    request.radius = 5000;
//    request.keywords = @"";
//    request.requireExtension    = YES;
    NSLog(@"request%@",request);
    [self.search AMapPOIAroundSearch:request];

}



/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    NSLog(@"onPOISearchDone%@",SearchType);
    NSLog(@"onPOISearchDone%@",response.pois);

    if (response.pois.count == 0)
    {
        NSArray *result = [NSArray array];
        if([@"poiSearchKeyWord" isEqual: SearchType]){
            [self sendEventWithName:@"onPoiSearched" body: @{@"result":result}];
        }else{
            [self sendEventWithName:@"onPoiSearchBound" body: @{@"result":result}];
        }
        return;
    }

    NSArray *result = [AMapPOI mj_keyValuesArrayWithObjectArray:response.pois];
    if([@"poiSearchKeyWord" isEqual: SearchType]){
        [self sendEventWithName:@"onPoiSearched" body: @{@"result":result}];
    }else{
        [self sendEventWithName:@"onPoiSearchBound" body: @{@"result":result}];
    }

}

/**
 支持的发送事件
 */
- (NSArray<NSString *> *)supportedEvents{
    return @[@"onPoiSearched",@"onPoiSearchBound"];
}








@end
