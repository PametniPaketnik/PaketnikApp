package com.example.paketnikapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.paketnikapp.databinding.FragmentMailboxBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MailboxFragment : Fragment(R.layout.fragment_mailbox) {
    private var _binding: FragmentMailboxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMailboxBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = DataHolder.id

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val mailbox = HttpCalls.getMailboxById(id.toString())
                if (mailbox != null) {
                    //Toast.makeText(context, mailbox.street, Toast.LENGTH_SHORT).show()
                    binding.textViewBoxID.text = mailbox.boxID
                    binding.textViewStreet.text = mailbox.street
                    binding.textViewPostcode.text = mailbox.postcode.toString()
                    binding.textViewPost.text = mailbox.post
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error retrieving mailbox", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}