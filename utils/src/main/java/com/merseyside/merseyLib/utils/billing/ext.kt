package com.merseyside.merseyLib.utils.billing

import com.android.billingclient.api.SkuDetails

fun SkuDetails.subscriptionPeriod(): Period {
    return Period.stringToPeriod(this.subscriptionPeriod)
}

fun SkuDetails.freeTrialPeriod(): Period {
    return Period.stringToPeriod(this.freeTrialPeriod)
}

fun SkuDetails.introductoryPeriod(): Period {
    return Period.stringToPeriod(this.introductoryPricePeriod)
}

