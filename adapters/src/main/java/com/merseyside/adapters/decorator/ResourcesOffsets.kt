package com.merseyside.adapters.decorator

import androidx.annotation.DimenRes
import com.merseyside.adapters.R

data class ResourcesOffsets(
    @DimenRes val left: Int = R.dimen.decorator_zero_size,
    @DimenRes val top: Int = R.dimen.decorator_zero_size,
    @DimenRes val right: Int = R.dimen.decorator_zero_size,
    @DimenRes val bottom: Int = R.dimen.decorator_zero_size
)