package com.alokomkar.porter

/**
 * Created by Alok on 09/04/18.
 */
interface BaseView {
    fun showProgress( message : String )
    fun hideProgress()
    fun onError( error : String )
}