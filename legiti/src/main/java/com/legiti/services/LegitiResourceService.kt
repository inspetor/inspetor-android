package com.legiti.services

import com.legiti.helpers.*


interface LegitiResourceService {

    /**
     * Send user data to Legiti
     *
     * @param account_id;
     * @param action
     * @return void
     */
    fun trackUserAction(data: HashMap<String, String?>, action: UserAction): Boolean

    /**
     * Send auth data to Legiti
     *
     * @param account_email;
     * @param action
     * @return void
     */
    fun trackUserAuthAction(data: HashMap<String, String?>,  action: AuthAction): Boolean

    /**
     * Send pass recovery data to Legiti
     *
     * @param action
     * @return void
     */
    fun trackPasswordRecoveryAction(data: HashMap<String, String?>, action: PasswordAction): Boolean

    /**
     * Send pass recovery data to Legiti
     *
     * @param action
     * @return void
     */
    fun trackPasswordResetAction(data: HashMap<String, String?>, action: PasswordAction): Boolean

    /**
     * Send Order data to Legiti
     *
     * @param sale_id;
     * @param action
     * @return void
     */
    fun trackOrderAction(data: HashMap<String, String?>, action: OrderAction): Boolean

    /**
     * Send page data to Legiti
     *
     * @param page_title;
     * @return void
     */
    fun trackPageView(page_title: String): Boolean

}