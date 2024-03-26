package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;
import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;
import static ca.flymile.API.PassengerJsonProvider.getPassengersJson;

public class RequestHandlerDelta {
    public static String requestHandlerDelta(String origin, String destination, String date, int numPassengers, boolean upperCabin) {

        String passengersJson = getPassengersJson(numPassengers);
        String cabin = upperCabin ? "D1" : "BE";
        String requestBody = String.format("""
                            {
                                 "variables": {
                                   "offerSearchCriteria": {
                                     "productGroups": [
                                       {
                                         "productCategoryCode": "FLIGHTS"
                                       }
                                     ],
                                     "offersCriteria": {
                                       "showContentLinks": false,
                                       "resultsPageNum": 1,
                                       "resultsPerRequestNum": 20,
                                       "preferences": {
                                         "refundableOnly": false,
                                         "showGlobalRegionalUpgradeCertificate": true,
                                         "nonStopOnly": false
                                       },
                                       "pricingCriteria": {
                                         "priceableIn": [
                                           "MILES"
                                         ]
                                       },
                                       "flightRequestCriteria": {
                                         "currentTripIndexId": "0",
                                         "sortableOptionId": null,
                                         "selectedOfferId": "",
                                         "searchOriginDestination": [
                                           {
                                             "departureLocalTs": "%sT00:00:00",
                                             "destinations": [
                                               {
                                                 "airportCode": "%s"
                                               }
                                             ],
                                             "origins": [
                                               {
                                                 "airportCode": "%s"
                                               }
                                             ]
                                           }
                                         ],
                                         "sortByBrandId": "%s"
                                       }
                                     },
                                     "customers": %s
                                   }
                                 },
                                 "query": "query ($offerSearchCriteria: OfferSearchCriteriaInput!) {\\n  gqlSearchOffers(offerSearchCriteria: $offerSearchCriteria) {\\n    offerResponseId\\n    gqlOffersSets {\\n      trips {\\n        tripId\\n        scheduledDepartureLocalTs\\n        scheduledArrivalLocalTs\\n        originAirportCode\\n        destinationAirportCode\\n        stopCnt\\n        flightSegment {\\n          aircraftTypeCode\\n          dayChange\\n          destinationAirportCode\\n          flightLeg {\\n            legId\\n            dayChange\\n            destinationAirportCode\\n            feeRestricted\\n            scheduledArrivalLocalTs\\n            scheduledDepartureLocalTs\\n            layover {\\n              destinationAirportCode\\n              layoverAirportCode\\n              layoverDuration {\\n                hourCnt\\n                minuteCnt\\n              }\\n              departureFlightNum\\n              equipmentChange\\n              originAirportCode\\n              scheduledArrivalLocalTs\\n              scheduledDepartureLocalTs\\n            }\\n            operatedByOwnerCarrier\\n            redEye\\n            operatingCarrier {\\n              carrierCode\\n              carrierName\\n            }\\n            marketingCarrier {\\n              carrierCode\\n              carrierName\\n            }\\n            earnLoyaltyMiles\\n            loyaltyMemberBenefits\\n            dominantLeg\\n            duration {\\n              dayCnt\\n              hourCnt\\n              minuteCnt\\n            }\\n            originAirport {\\n              airportTerminals {\\n                terminalId\\n              }\\n            }\\n            destinationAirport {\\n              airportTerminals {\\n                terminalId\\n              }\\n            }\\n            originAirportCode\\n            aircraft {\\n              fleetTypeCode\\n              subFleetTypeCode\\n              newSubFleetType\\n            }\\n            carrierCode\\n            distance {\\n              unitOfMeasure\\n              unitOfMeasureCnt\\n            }\\n          }\\n          layover {\\n            destinationAirportCode\\n            layoverAirportCode\\n            layoverDuration {\\n              hourCnt\\n              minuteCnt\\n            }\\n            departureFlightNum\\n            equipmentChange\\n            originAirportCode\\n            scheduledArrivalLocalTs\\n            scheduledDepartureLocalTs\\n          }\\n          marketingCarrier {\\n            carrierCode\\n            carrierNum\\n          }\\n          operatingCarrier {\\n            carrierCode\\n            carrierNum\\n            carrierName\\n          }\\n          pendingGovtApproval\\n          destinationCityCode\\n          flightSegmentNum\\n          originAirportCode\\n          originCityCode\\n          scheduledArrivalLocalTs\\n          scheduledDepartureLocalTs\\n          aircraft {\\n            fleetTypeCode\\n            subFleetTypeCode\\n            newSubFleetType\\n          }\\n        }\\n        totalTripTime {\\n          dayCnt\\n          hourCnt\\n          minuteCnt\\n        }\\n        summarizedProductId\\n      }\\n      additionalOfferSetProperties {\\n        globalUpgradeCertificateTripStatus {\\n          brandId\\n          upgradeAvailableStatusProductId\\n        }\\n        regionalUpgradeCertificateTripStatus {\\n          brandId\\n          upgradeAvailableStatusProductId\\n        }\\n        offerSetId\\n        seatReferenceId\\n        discountInfo {\\n          discountPct\\n          discountTypeCode\\n          nonDiscountedOffersAvailable\\n        }\\n        promotionsInfo {\\n          promotionalCode\\n          promotionalPct\\n        }\\n        discountInEligibilityList {\\n          code\\n          reason\\n        }\\n      }\\n      offerSetBadges {\\n        brandId\\n      }\\n      offers {\\n        offerId\\n        additionalOfferProperties {\\n          offered\\n          fareType\\n          dominantSegmentBrandId\\n          priorityNum\\n          soldOut\\n          unavailableForSale\\n          refundable\\n          offerBadges {\\n            brandId\\n          }\\n          payWithMilesEligible\\n          discountAvailable\\n          travelPolicyStatus\\n        }\\n        soldOut\\n        offerItems {\\n          retailItems {\\n            retailItemMetaData {\\n              fareInformation {\\n                brandByFlightLegs {\\n                  brandId\\n                  cosCode\\n                  tripId\\n                  product {\\n                    brandId\\n                    typeCode\\n                  }\\n                  globalUpgradeCertificateLegStatus {\\n                    upgradeAvailableStatusProductId\\n                  }\\n                  regionalUpgradeCertificateLegStatus {\\n                    upgradeAvailableStatusProductId\\n                  }\\n                  flightSegmentNum\\n                  flightLegNum\\n                }\\n                discountInEligibilityList {\\n                  code\\n                  reason\\n                }\\n                availableSeatCnt\\n                farePrice {\\n                  discountsApplied {\\n                    pct\\n                    code\\n                    description\\n                    reason\\n                    amount {\\n                      currencyEquivalentPrice {\\n                        currencyAmt\\n                      }\\n                      milesEquivalentPrice {\\n                        mileCnt\\n                        discountMileCnt\\n                      }\\n                    }\\n                  }\\n                  totalFarePrice {\\n                    currencyEquivalentPrice {\\n                      roundedCurrencyAmt\\n                      formattedCurrencyAmt\\n                    }\\n                    milesEquivalentPrice {\\n                      mileCnt\\n                      cashPlusMilesCnt\\n                      cashPlusMiles\\n                    }\\n                  }\\n                  originalTotalPrice {\\n                    currencyEquivalentPrice {\\n                      roundedCurrencyAmt\\n                      formattedCurrencyAmt\\n                    }\\n                    milesEquivalentPrice {\\n                      mileCnt\\n                      cashPlusMilesCnt\\n                      cashPlusMiles\\n                    }\\n                  }\\n                  promotionalPrices {\\n                    price {\\n                      currencyEquivalentPrice {\\n                        roundedCurrencyAmt\\n                        formattedCurrencyAmt\\n                      }\\n                      milesEquivalentPrice {\\n                        mileCnt\\n                        cashPlusMilesCnt\\n                        cashPlusMiles\\n                      }\\n                    }\\n                  }\\n                }\\n              }\\n            }\\n          }\\n        }\\n      }\\n    }\\n    offerDataList {\\n      responseProperties {\\n        discountInfo {\\n          discountPct\\n          discountTypeCode\\n          nonDiscountedOffersAvailable\\n        }\\n        promotionsInfo {\\n          promotionalCode\\n          promotionalPct\\n        }\\n        discountInEligibilityList {\\n          code\\n          reason\\n        }\\n        resultsPerRequestNum\\n        pageResultCnt\\n        resultsPageNum\\n        sortOptionsList {\\n          sortableOptionDesc\\n          sortableOptionId\\n        }\\n        tripTypeText\\n      }\\n      offerPreferences {\\n        stopCnt\\n        destinationAirportCode\\n        connectionTimeRange {\\n          maximumNum\\n          minimumNum\\n        }\\n        originAirportCode\\n        flightDurationRange {\\n          maximumNum\\n          minimumNum\\n        }\\n        layoverAirportCode\\n        totalMilesRange {\\n          maximumNum\\n          minimumNum\\n        }\\n        totalPriceRange {\\n          maximumNum\\n          minimumNum\\n        }\\n      }\\n      retailItemDefinitionList {\\n        brandType\\n        retailItemBrandId\\n        refundable\\n        retailItemPriorityText\\n      }\\n      pricingOptions {\\n        pricingOptionDetail {\\n          currencyCode\\n        }\\n      }\\n    }\\n    gqlSelectedOfferSets {\\n      trips {\\n        tripId\\n        scheduledDepartureLocalTs\\n        scheduledArrivalLocalTs\\n        originAirportCode\\n        destinationAirportCode\\n        stopCnt\\n        flightSegment {\\n          destinationAirportCode\\n          marketingCarrier {\\n            carrierCode\\n            carrierNum\\n          }\\n          operatingCarrier {\\n            carrierCode\\n            carrierNum\\n          }\\n          flightSegmentNum\\n          originAirportCode\\n          scheduledArrivalLocalTs\\n          scheduledDepartureLocalTs\\n          aircraft {\\n            fleetTypeCode\\n            subFleetTypeCode\\n            newSubFleetType\\n          }\\n          flightLeg {\\n            destinationAirportCode\\n            feeRestricted\\n            layover {\\n              destinationAirportCode\\n              layoverAirportCode\\n              layoverDuration {\\n                hourCnt\\n                minuteCnt\\n              }\\n              departureFlightNum\\n              equipmentChange\\n              originAirportCode\\n              scheduledArrivalLocalTs\\n              scheduledDepartureLocalTs\\n            }\\n            operatedByOwnerCarrier\\n            redEye\\n            operatingCarrier {\\n              carrierCode\\n              carrierName\\n            }\\n            marketingCarrier {\\n              carrierCode\\n              carrierName\\n            }\\n            earnLoyaltyMiles\\n            loyaltyMemberBenefits\\n            dominantLeg\\n            duration {\\n              dayCnt\\n              hourCnt\\n              minuteCnt\\n            }\\n            originAirport {\\n              airportTerminals {\\n                terminalId\\n              }\\n            }\\n            destinationAirport {\\n              airportTerminals {\\n                terminalId\\n              }\\n            }\\n            originAirportCode\\n            aircraft {\\n              fleetTypeCode\\n              subFleetTypeCode\\n              newSubFleetType\\n            }\\n            carrierCode\\n            distance {\\n              unitOfMeasure\\n              unitOfMeasureCnt\\n            }\\n            scheduledArrivalLocalTs\\n            scheduledDepartureLocalTs\\n            dayChange\\n          }\\n        }\\n        totalTripTime {\\n          dayCnt\\n          hourCnt\\n          minuteCnt\\n        }\\n      }\\n      offers {\\n        additionalOfferProperties {\\n          dominantSegmentBrandId\\n          fareType\\n        }\\n        soldOut\\n        offerItems {\\n          retailItems {\\n            retailItemMetaData {\\n              fareInformation {\\n                brandByFlightLegs {\\n                  tripId\\n                  brandId\\n                  cosCode\\n                }\\n              }\\n            }\\n          }\\n        }\\n      }\\n      additionalOfferSetProperties {\\n        seatReferenceId\\n      }\\n    }\\n    contentLinks {\\n      type\\n      variables {\\n        brandProductMapping\\n      }\\n    }\\n  }\\n}"
                               }
                  """, date, destination, origin, cabin, passengersJson);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://offer-api-prd.delta.com/prd/rm-offer-gql"))
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-CA,en-US;q=0.9,en;q=0.8")
                .header("Content-Type", "application/json")
                .header("Authorization", "GUEST")
                .header("Origin", "https://www.delta.com")
                .header("TransactionId", UUID.randomUUID().toString())
                .header("applicationId", "DC")
                .header("channelId", "DCOM")
                .header("Airline", "DL")
                .header("Referer", "https://www.delta.com/")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.1 Safari/605.1.15")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }

}


