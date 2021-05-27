package hu.leopph.inventoree


import LoadingFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var mAuth: FirebaseAuth
    private var _mBinding: FragmentRegisterBinding? = null
    private val mBinding: FragmentRegisterBinding get() = _mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        mBinding.registerButton.setOnClickListener(this::onRegisterPressed)
        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


    private fun onRegisterPressed(@Suppress("UNUSED_PARAMETER") view: View) {
        val email = mBinding.regEmailEdittext.text.toString()

        if (email.isBlank()) {
            Toast.makeText(requireContext(), R.string.email_blank_error, Toast.LENGTH_LONG).show()
            return
        }

        val pwd = mBinding.regPwdEdittext.text.toString()
        val pwdConf = mBinding.regPwdConfEdittext.text.toString()

        if (pwd.isBlank() || pwdConf.isBlank()) {
            Toast.makeText(requireContext(), R.string.pwd_blank_error, Toast.LENGTH_LONG).show()
            return
        }

        if (pwd != pwdConf) {
            Toast.makeText(requireContext(), R.string.pwd_conf_dont_match_error, Toast.LENGTH_LONG).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(
                        Intent(requireContext(), ProductListActivity::class.java)
                            .putExtra("UID", mAuth.currentUser?.uid)
                    )

                    swapFragment<LoginFragment>(R.id.welcome_fragment_container)
                }
                else {
                    Toast.makeText(requireContext(), R.string.firebase_reg_error, Toast.LENGTH_LONG).show()
                    swapFragment<RegisterFragment>(R.id.welcome_fragment_container)
                }
            }

        swapFragment<LoadingFragment>(R.id.welcome_fragment_container)
    }


    private inline fun <reified T: Fragment> swapFragment(layoutID: Int) {
        requireActivity().supportFragmentManager.commit {
            replace<T>(layoutID)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}