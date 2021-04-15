package com.myniprojects.towatch.ui.fragments

import androidx.fragment.app.Fragment
import com.myniprojects.towatch.R
import com.myniprojects.towatch.databinding.FragmentSearchBinding
import com.myniprojects.towatch.databinding.FragmentWatchedBinding
import com.myniprojects.towatch.utils.helper.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchedFragment : Fragment(R.layout.fragment_watched)
{
    private val binding by viewBinding(FragmentWatchedBinding::bind)
}