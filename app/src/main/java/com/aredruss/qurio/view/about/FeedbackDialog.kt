package com.aredruss.qurio.view.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aredruss.qurio.R
import com.aredruss.qurio.databinding.DialogFeedbackBinding
import com.aredruss.qurio.view.utils.composeEmail
import com.aredruss.qurio.view.utils.openLink
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeedbackDialog : BottomSheetDialogFragment() {

    lateinit var binding: DialogFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_BottomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFeedbackBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cancelTv.setOnClickListener { dismiss() }

            githubBtn.setOnClickListener {
                activity?.openLink(GITHUB_ISSUE_LINK)
            }
            twitterBtn.setOnClickListener {
                activity?.openLink(TWITTER_MENTION_LINK)
            }
            emailBtn.setOnClickListener {
                activity?.composeEmail()
            }
        }
    }

    companion object {
        const val FEEDBACK_DIALOG_TAG = "feedback"
        private const val GITHUB_ISSUE_LINK = "https://github.com/Aredruss/Qurio/issues/new"
        private const val TWITTER_MENTION_LINK = "https://twitter.com/intent/tweet?text=@Aredruss"
    }
}