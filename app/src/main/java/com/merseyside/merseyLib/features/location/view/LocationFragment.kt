package com.merseyside.merseyLib.features.location.view

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentLocationBinding
import com.merseyside.merseyLib.features.location.di.DaggerLocationComponent
import com.merseyside.merseyLib.features.location.di.LocationModule
import com.merseyside.merseyLib.features.location.model.LocationViewModel
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.utils.fragment.onBackPressedDispatcher.setOnBackPressedCallback
import com.merseyside.utils.view.ext.onClick
import com.merseyside.utils.isPermissionsGranted
import com.merseyside.utils.requestPermissions
import com.merseyside.utils.location.LocationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationFragment : BaseSampleFragment<FragmentLocationBinding, LocationViewModel>() {

    @Inject
    lateinit var locationManager: LocationManager

    private var job: Job? = null
        set(value) {
            field = value
            backPressedCallback.isEnabled = value != null
        }

    override fun getLayoutId() = R.layout.fragment_location

    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerLocationComponent.builder()
            .appComponent(appComponent)
            .locationModule(getLocationModule(bundle))
            .build().inject(this)
    }

    private fun getLocationModule(bundle: Bundle?) = LocationModule(this)
    override fun getTitle(context: Context) = getString(R.string.location_title)
    override fun getBindingVariable() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressedCallback = setOnBackPressedCallback(enabled = false) {
            safeLet(job) {
                stopCollectingCoords()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding().button.onClick {
            if (job == null) {
                startCollectingCoors()
            } else {
                stopCollectingCoords()
            }
        }
    }

    private fun startCollectingCoors() {
        requireBinding().button.text = getString(R.string.stop_getting_location)
        job = lifecycleScope.launch {
            val flow = locationManager.getLocationFlow()
            flow.collect {
                Toast.makeText(
                    context,
                    "lat = ${it.latitude} lon = ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun stopCollectingCoords() {
        requireBinding().button.text = getString(R.string.start_getting_location)
        job?.let {
            it.cancel()
            job = null
        }
    }

    override fun onResume() {
        super.onResume()

        checkPermissions()
    }

    private fun checkPermissions() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        with(requireContext()) {
            if (!isPermissionsGranted (permission)) {
                requestPermissions(permission, requestCode = REQUEST_CODE)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 1111
    }
}