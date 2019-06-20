package com.example.marek.sledzeniepaczek.couriers

import java.net.URL

enum class Couriers(val courierName: String, val trackingUrl: String) {
    //http://webtrack.dhlglobalmail.com/?trackingnumber=GM295322949114412899
//    DHL("DHL", "https://www.dhl.com.pl/en/express/tracking.html?AWB="),
//    FEDEX("FedEx", "https://www.fedex.com/apps/fedextrack/?action=track&locale=en_US&tracknumbers="),
//    INPOST("Inpost", "https://inpost.pl/sledzenie-przesylek?number="),
//    UPS("UPS", "https://www.ups.com/track?requester=NES&agreeTerms=yes&tracknum="),
//    POCZTA_POLSKA("Poczta polska", "https://emonitoring.poczta-polska.pl/?numer=");
    DHL("DHL", "dhl/"),
    FEDEX("FedEx", "fedex/"),
    INPOST("Inpost", "inpost/"),
    UPS("UPS", "ups/"),
    POCZTA_POLSKA("Poczta polska", "pp/");

    fun websiteUrl(packageNumber: String): URL {
        val serverUrl = "http://167.99.242.128:5000/$trackingUrl"
        val link = "$serverUrl$packageNumber"
        return URL(link)
    }

    companion object {
        fun findCourier(courierName: String): Couriers {
            var courier = DHL
            for (c in Couriers.values()) {
                if (c.courierName == courierName) {
                    courier = c
                    break
                }
            }
            return courier
        }
    }
}