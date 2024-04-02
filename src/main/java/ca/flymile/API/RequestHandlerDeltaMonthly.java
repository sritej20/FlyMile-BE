package ca.flymile.API;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import static ca.flymile.API.RequestBuilder.buildRequestBodyDeltaMonthly;

public class RequestHandlerDeltaMonthly {
    private static final String REQUEST_URL = "https://offer-api-prd.delta.com/prd/rm-offer-gql";
    private static final String REQUEST_TEMPLATE = """
            {
                "variables": {
                    "offerSearchCriteria": {
                        "productGroups": [
                            {
                                "productCategoryCode": "FLIGHTS"
                            }
                        ],
                        "customers": %s,
                        "offersCriteria": {
                            "pricingCriteria": {
                                "priceableIn": ["MILES"]
                            },
                            "preferences": {
                                "nonStopOnly": %b,
                                "refundableOnly": false
                            },
                            "flightRequestCriteria": {
                                "sortByBrandId": "%s",
                                "calendarSearch": true,
                                "searchOriginDestination": [
                                    {
                                        "departureLocalTs": "%sT00:00:00",
                                        "destinations": [{"airportCode": "%s"}],
                                        "origins": [{"airportCode": "%s"}]
                                    }
                                ]
                            }
                        }
                    }
                },
                "query": "query ($offerSearchCriteria: OfferSearchCriteriaInput!) {\\n  gqlSearchOffers(offerSearchCriteria: $offerSearchCriteria) {\\n    offerResponseId\\n    gqlOffersSets {\\n      offers {\\n        offerId\\n        offerItems {\\n          retailItems {\\n            retailItemMetaData {\\n              fareInformation {\\n                priceCalendar {\\n                  priceCalendarDate\\n                }\\n              }\\n            }\\n          }\\n        }\\n        additionalOfferProperties {\\n          offered\\n          totalTripStopCnt\\n          dayOfMonthText\\n          monthText\\n          lowestFareInCalendar\\n        }\\n        soldOut\\n        offerPricing {\\n          discountsApplied {\\n            code\\n            pct\\n            amount {\\n              currencyEquivalentPrice {\\n                roundedCurrencyAmt\\n              }\\n              milesEquivalentPrice {\\n                mileCnt\\n              }\\n            }\\n          }\\n          totalAmt {\\n            currencyEquivalentPrice {\\n              roundedCurrencyAmt\\n            }\\n            milesEquivalentPrice {\\n              mileCnt\\n            }\\n          }\\n          originalTotalAmt {\\n            currencyEquivalentPrice {\\n              roundedCurrencyAmt\\n            }\\n            milesEquivalentPrice {\\n              mileCnt\\n            }\\n          }\\n          promotionalPrices {\\n            pct\\n            code\\n            price {\\n              currencyEquivalentPrice {\\n                roundedCurrencyAmt\\n              }\\n              milesEquivalentPrice {\\n                mileCnt\\n              }\\n            }\\n          }\\n        }\\n      }\\n      additionalOfferSetProperties {\\n        discountInfo {\\n          discountPct\\n          discountTypeCode\\n          nonDiscountedOffersAvailable\\n        }\\n        promotionsInfo {\\n          promotionalCode\\n          promotionalPct\\n        }\\n      }\\n    }\\n    offerDataList {\\n      pricingOptions {\\n        pricingOptionDetail {\\n          currencyCode\\n        }\\n      }\\n      responseProperties {\\n        discountInfo {\\n          discountPct\\n          discountTypeCode\\n        }\\n        promotionsInfo {\\n          promotionalCode\\n          promotionalPct\\n        }\\n        tripTypeText\\n        calendarMonthText\\n      }\\n    }\\n  }\\n}"
            }""";

    public static String requestHandlerDeltaMonthly(String origin, String destination, String departureDate, int numPassengers, boolean upperCabin, boolean nonStopOnly) {
        HttpClient CLIENT = HttpClient.newHttpClient();
       /*HttpClient CLIENT = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress("us.smartproxy.com", 10000)))
                .build();*/
        return buildRequestBodyDeltaMonthly(origin, destination, upperCabin, departureDate, numPassengers, REQUEST_TEMPLATE, nonStopOnly, REQUEST_URL, CLIENT);
    }
}
