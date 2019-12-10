//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context
import android.os.Build
import android.provider.Settings.Secure
import android.util.Base64
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import com.scottyab.rootbeer.RootBeer
import java.net.NetworkInterface.getNetworkInterfaces
import android.net.NetworkCapabilities
import android.net.ConnectivityManager
import com.inspetor.helpers.*
import com.inspetor.services.InspetorResourceService
import com.snowplowanalytics.snowplow.tracker.events.ScreenView
import java.security.MessageDigest

internal class InspetorResource(config: InspetorConfig, androidContext: Context):
    InspetorResourceService {

    private var spTracker: Tracker
    private var androidContext: Context
    private var inspetorDeviceData: InspetorDeviceData
    private var inspetorDeviceIdContext: SelfDescribingJson

    init {
        SnowplowManager.init(config)
        this.androidContext = androidContext
        this.spTracker = SnowplowManager.setupTracker(this.androidContext) ?: throw fail("Inspetor Exception 9000: Internal error.")
        this.inspetorDeviceData = InspetorDeviceData(androidContext)
        this.inspetorDeviceIdContext = this.getFingerprintContext()
    }

    override fun trackAccountAction(data: HashMap<String, String?>, action: AccountAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_ACCOUNT_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackAccountAuthAction(data: HashMap<String, String?>, action: AuthAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_AUTH_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackEventAction(data: HashMap<String, String?>, action: EventAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_EVENT_SCHEMA_VERSION,
            data,
            action.rawValue()
        )


        return true
    }

    override fun trackPasswordRecoveryAction(data: HashMap<String, String?>, action: PassRecoveryAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_PASS_RECOVERY_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackItemTransferAction(data: HashMap<String, String?>, action: TransferAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_TRANSFER_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackSaleAction(data: HashMap<String, String?>, action: SaleAction): Boolean {
        this.trackUnstructuredEvent(
            InspetorDependencies.FRONTEND_SALE_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackPageView(page_title: String): Boolean {
        val spContexts: ArrayList<SelfDescribingJson> = arrayListOf()
        this.inspetorDeviceIdContext.let{ spContexts.add(it) }

        this.spTracker.track(
            ScreenView.builder()
                .name(page_title)
                .id(UUID.randomUUID().toString())
                .customContext(spContexts)
                .build() ?: throw fail("Inspetor Exception 9000: Internal error.")
        )

        // Making sure there are no more events to be sent
        this.spTracker.emitter?.flush()

        return true
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String?>, action: String) {
        val inspetorData = SelfDescribingJson(schema, data)

        val spContexts: ArrayList<SelfDescribingJson> = arrayListOf(
            this.setupActionContext(action)
        )

        this.inspetorDeviceIdContext.let{ spContexts.add(it) }

        this.spTracker.track(
            SelfDescribing.builder()
                .eventData(inspetorData)
                .customContext(spContexts)
                .build() ?: throw fail("Inspetor Exception 9000: Internal error.")
        )

        // Making sure there are no more events to be sent
        this.spTracker.emitter?.flush()
    }

    private fun setupActionContext(action: String): SelfDescribingJson {
        val contextMap: HashMap<String, String>? = hashMapOf(
            "action" to action
        )

        return SelfDescribingJson(
            InspetorDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION,
            contextMap
        )

    }

    private fun fail(message: String): Throwable {
        throw Exception(message)
    }

    private fun getFingerprintContext(): SelfDescribingJson {
        val deviceData = this.inspetorDeviceData.getDeviceData()

        return SelfDescribingJson(
            InspetorDependencies.FRONTEND_FINGERPRINT_SCHEMA_VERSION,
            deviceData
        )
    }


}
