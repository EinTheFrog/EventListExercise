package com.example.eventcalendar.utils.extensions

import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment

fun Fragment.attachActionBarMenuProvider(menuProvider: MenuProvider) {
    activity?.addMenuProvider(menuProvider)
}

fun Fragment.detachActionBarMenuProvider(menuProvider: MenuProvider) {
    activity?.removeMenuProvider(menuProvider)
}