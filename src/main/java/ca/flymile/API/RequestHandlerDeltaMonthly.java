package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;
import static ca.flymile.API.PassengerJsonProvider.getPassengersJson;

public class RequestHandlerDeltaMonthly {
    public static String requestHandlerDeltaMonthly(String origin, String destination, String date, int numPassengers) {
        String passengersJson = getPassengersJson(numPassengers);

        String requestBody = String.format("""
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
                                "nonStopOnly": false,
                                "refundableOnly": false
                            },
                            "flightRequestCriteria": {
                                "sortByBrandId": "BE",
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
            }""", passengersJson, date, destination, origin);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://offer-api-prd.delta.com/prd/rm-offer-gql"))
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Content-Type", "application/json")
                .header("Authorization", "GUEST")
                .header("Origin", "https://www.delta.com")
                .header("TransactionId", UUID.randomUUID().toString())
                .header("applicationId", "DC")
                .header("channelId", "DCOM")
                .header("Airline", "DL")
                .header("Referer", "https://www.delta.com/")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Safari/605.1.15")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }
}
