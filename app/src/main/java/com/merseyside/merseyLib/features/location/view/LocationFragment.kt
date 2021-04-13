package com.merseyside.merseyLib.features.location.view

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentLocationBinding
import com.merseyside.merseyLib.features.location.di.DaggerLocationComponent
import com.merseyside.merseyLib.features.location.di.LocationModule
import com.merseyside.merseyLib.features.location.model.LocationViewModel
import com.merseyside.utils.PermissionManager
import com.merseyside.utils.ext.log
import com.merseyside.utils.ext.onClick
import com.merseyside.utils.service.LocationManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationFragment : BaseSampleFragment<FragmentLocationBinding, LocationViewModel>() {

    @Inject
    lateinit var locationManager: LocationManager

    override fun hasTitleBackButton() = true
    override fun getLayoutId() = R.layout.fragment_location

    override fun performInjection(bundle: Bundle?) {
        DaggerLocationComponent.builder()
            .appComponent(appComponent)
            .locationModule(getLocationModule(bundle))
            .build().inject(this)
    }

    private fun getLocationModule(bundle: Bundle?) = LocationModule(this)
    override fun getTitle(context: Context) = getString(R.string.location_title)
    override fun getBindingVariable() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().button.onClick {
            lifecycleScope.launch {
                locationManager.getLocation().log()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkPermissions()
    }

    private fun checkPermissions() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (!PermissionManager.isPermissionsGranted(context, permission)) {
            PermissionManager.requestPermissions(this, permission, requestCode = REQUEST_CODE)
        }
    }

    companion object {
        private const val REQUEST_CODE = 1111
    }
}