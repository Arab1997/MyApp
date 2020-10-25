package myway.myapplication.utils.bottomsheet

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import myway.myapplication.R

abstract class BottomSheetRoundedFragment(@LayoutRes val layoutId: Int) :
    BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initialize()
    }

    abstract fun initialize()

    fun setMaxHeight(height: Double) {
        val dm = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dm)
        val currentHeight = (dm.heightPixels * height).toInt()
        dialog?.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheetInternal: View? =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.apply {
                val behavior = BottomSheetBehavior.from(this)
                if (currentHeight != 0) {
                    behavior.peekHeight = currentHeight
                    this.layoutParams.height = currentHeight
                }
                behavior.skipCollapsed = false
            }
        }
    }

    fun closeSheet() = this.dismiss()

}
