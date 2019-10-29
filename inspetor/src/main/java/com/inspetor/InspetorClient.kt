//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context

class InspetorClient() : InspetorService {
    private var inspetorResource: InspetorResource?
    private var inspetorConfig: InspetorConfig?
    private var doneSetup: Boolean
    private var androidContext: Context?

    init {
        this.inspetorResource = null
        this.inspetorConfig = null
        this.doneSetup = false
        this.androidContext = null
    }

    override fun setup(trackerName: String, appId: String, devEnv: Boolean?, inspetorEnv: Boolean?) {
        if (appId.isEmpty() || trackerName.isEmpty()) {
            throw Exception("Exception 9001: appId and trackerName are required parameters.")
        }

        require(validateTrackerName(trackerName)) { "Inspetor Exception 9002: trackerName should have 2 terms (e.g. \"tracker.name\")." }

        this.inspetorConfig = InspetorConfig(trackerName, appId, devEnv, inspetorEnv)

        if (this.androidContext != null) {
            this.setContext(androidContext!!)
            doneSetup = true
        } else {
            throw Exception("Inspetor Exception 9000: Could not get the context please pass it to the setContext function.")
        }

    }

    override fun setContext(context: Context) {
        val config = this.inspetorConfig ?: return

        if (this.androidContext == null) {
            this.androidContext = context
        }

        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        this.inspetorResource = InspetorResource(config, context)
    }

    override fun isConfigured(): Boolean {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return doneSetup
    }

    override fun trackLogin(account_email: String, account_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackAccountAuthAction(account_email, account_id, AuthAction.ACCOUNT_LOGIN_ACTION)
    }

    override fun trackLogout(account_email: String, account_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackAccountAuthAction(account_email, account_id, AuthAction.ACCOUNT_LOGOUT_ACTION)
    }

    override fun trackAccountCreation(account_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackAccountAction(account_id, AccountAction.ACCOUNT_CREATE_ACTION)
    }

    override fun trackAccountUpdate(account_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackAccountAction(account_id, AccountAction.ACCOUNT_UPDATE_ACTION)
    }

    override fun trackAccountDeletion(account_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackAccountAction(account_id, AccountAction.ACCOUNT_DELETE_ACTION)
    }

    override fun trackSaleCreation(sale_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackSaleAction(sale_id, SaleAction.SALE_CREATE_ACTION)
    }

    override fun trackSaleUpdate(sale_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackSaleAction(sale_id, SaleAction.SALE_UPDATE_STATUS_ACTION)
    }

    override fun trackEventCreation(event_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackEventAction(event_id, EventAction.EVENT_CREATE_ACTION)
    }

    override fun trackEventUpdate(event_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackEventAction(event_id, EventAction.EVENT_UPDATE_ACTION)
    }

    override fun trackEventDeletion(event_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackEventAction(event_id, EventAction.EVENT_DELETE_ACTION)
    }

    override fun trackItemTransferCreation(transfer_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackItemTransferAction(transfer_id, TransferAction.TRANSFER_CREATE_ACTION)
    }

    override fun trackItemTransferUpdate(transfer_id: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackItemTransferAction(transfer_id, TransferAction.TRANSFER_UPDATE_STATUS_ACTION)
    }

    override fun trackPasswordReset(accountEmail: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackPasswordRecoveryAction(accountEmail, PassRecoveryAction.PASSWORD_RESET_ACTION)
    }

    override fun trackPasswordRecovery(accountEmail: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackPasswordRecoveryAction(accountEmail, PassRecoveryAction.PASSWORD_RECOVERY_ACTION)
    }

    override fun trackPageView(page_title: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackPageView(page_title)
    }

    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorDependencies.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }

    private fun hasConfig(): Boolean {
        return (!inspetorConfig?.appId.isNullOrBlank() && !inspetorConfig?.trackerName.isNullOrBlank())
    }

    internal fun setContextWithoutConfig(context: Context?): Boolean {
        if (context != null) {
            this.androidContext = context
            return true
        }

        return false
    }

}