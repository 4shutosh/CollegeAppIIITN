package com.college.app.utils

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionRequester(
    private val fragment: Fragment,
    private val permission: String,
    onDenied: () -> Unit = {},
    onShowRationale: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> onGranted()
                fragment.shouldShowRequestPermissionRationale(permission) -> onShowRationale()
                else -> onDenied()
            }
        }

    fun runWithPermission(onGranted: () -> Unit) {
        this.onGranted = onGranted
        launcher.launch(permission)
    }
}

class PermissionRequesterList(
    private val fragment: Fragment,
    private val permissions: Array<String>,
    onDenied: () -> Unit = {},
    onShowRationale: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                when {
                    it.value -> onGranted()
                    fragment.shouldShowRequestPermissionRationale(it.key) -> onShowRationale()
                    else -> onDenied()
                }
            }

        }

    fun runWithPermissions(onGranted: () -> Unit) {
        this.onGranted = onGranted
        launcher.launch(permissions)
    }
}


// add go to settings and other functions here


