package com.aredruss.qurio.view.about

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aredruss.qurio.BuildConfig
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.FragmentAboutBinding
import com.aredruss.qurio.view.about.FeedbackDialog.Companion.FEEDBACK_DIALOG_TAG
import com.aredruss.qurio.view.utils.BaseFragment
import com.aredruss.qurio.view.utils.showSingle
import com.aredruss.qurio.view.utils.viewBinding

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding(FragmentAboutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.apply {
            versionTv.text = BuildConfig.VERSION_NAME
            feedbackTv.setOnClickListener {
                FeedbackDialog().showSingle(childFragmentManager, FEEDBACK_DIALOG_TAG)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }
}