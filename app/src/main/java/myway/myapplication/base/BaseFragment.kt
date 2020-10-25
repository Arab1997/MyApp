package myway.myapplication.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import myway.myapplication.R
import myway.myapplication.ui.activities.MainActivity
import myway.myapplication.utils.pereferences.SharedManager

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {


    protected lateinit var mainActivity: MainActivity
    protected lateinit var viewModel: BaseViewModel
    protected lateinit var sharedManager: SharedManager
    protected var enableCustomBackPress = false

    override fun onAttach(context: Context) {
        mainActivity = (requireActivity() as MainActivity)
        viewModel = mainActivity.viewModel
    //    sharedManager = mainActivity.sharedManager
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()

        setFocus(view)

        observe()

        observeError()
    }

    abstract fun initialize()

    fun addFragment(
        fragment: Fragment,
        addBackStack: Boolean = true, @IdRes id: Int = parentLayoutId(),
        tag: String = fragment.hashCode().toString()
    ) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            if (addBackStack && !fragment.isAdded) addToBackStack(tag)
            setCustomAnimations(
                R.anim.enter_from_bottom,
                R.anim.exit_to_top,
                R.anim.enter_from_top,
                R.anim.exit_to_bottom
            )
            add(id, fragment)
        }
    }

    fun replaceFragment(fragment: Fragment, @IdRes id: Int = navLayoutId()) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            replace(id, fragment)
        }
    }

    fun finishFragment() {
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    fun popInclusive(name: String? = null, flags: Int = FragmentManager.POP_BACK_STACK_INCLUSIVE) {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate(name, flags)
    }

    protected open fun onFragmentBackButtonPressed() {
    }

    protected open fun observe() {
    }

    protected fun showProgress(show: Boolean) {
        mainActivity.showProgress(show)
    }

    protected fun hideKeyboard() {
        view?.let {
            val imm =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showProgress(false)
        })
    }

    private fun setFocus(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (enableCustomBackPress) onFragmentBackButtonPressed()
                    else activity?.onBackPressed()
                }
                enableCustomBackPress = false
                true
            }
        }
    }

    fun windowAdjustPan() =
        mainActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    fun windowAdjustResize() =
        mainActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    @SuppressLint("CheckResult")
    fun newThread(action: () -> Unit) {
        viewModel.newThread { action() }
    }

    private val baseHandler = Handler()
    private var baseRunnable = Runnable {
    }

    fun removePreviousCallback(action: () -> Unit, millis: Long = 1000) {
        baseHandler.removeCallbacks(baseRunnable)
        baseRunnable = Runnable { action() }
        baseHandler.postDelayed(baseRunnable, millis)
    }



}

fun FragmentActivity.initialFragment(fragment: BaseFragment, showAnim: Boolean = false) {
    val containerId = ViewModelProviders.of(this)[BaseViewModel::class.java].parentLayoutId
    supportFragmentManager.commit(allowStateLoss = true) {
        if (showAnim)
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        replace(containerId, fragment)
    }
}

fun FragmentActivity.finishFragment() {
    supportFragmentManager.popBackStack()
}

fun BaseFragment.parentLayoutId() =
    ViewModelProviders.of(activity!!)[BaseViewModel::class.java].parentLayoutId

fun BaseFragment.navLayoutId() =
    ViewModelProviders.of(activity!!)[BaseViewModel::class.java].navLayoutId