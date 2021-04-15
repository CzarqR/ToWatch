package com.myniprojects.towatch.ui.fragments

import androidx.fragment.app.Fragment
import com.myniprojects.towatch.R
import com.myniprojects.towatch.databinding.FragmentSearchBinding
import com.myniprojects.towatch.databinding.FragmentTowatchBinding
import com.myniprojects.towatch.databinding.FragmentWatchedBinding
import com.myniprojects.towatch.utils.helper.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToWatchFragment : Fragment(R.layout.fragment_towatch)
{
    private val binding by viewBinding(FragmentTowatchBinding::bind)
}