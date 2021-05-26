package hu.leopph.inventoree


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
import hu.leopph.inventoree.databinding.FragmentLoginBinding


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var mAuth: FirebaseAuth
    private var _mBinding: FragmentLoginBinding? = null
    private val mBinding: FragmentLoginBinding get() = _mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentLoginBinding.inflate(inflater, container, false)
        mBinding.loginButton.setOnClickListener(this::onLoginPressed)
        mBinding.goToRegisterButton.setOnClickListener(this::onGoToRegister)
        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


    private fun onLoginPressed(@Suppress("UNUSED_PARAMETER") view: View) {
        if (mBinding.unameEdittext.text.toString().isBlank() || mBinding.unameEdittext.text.toString().isBlank())
            return

        mAuth.signInWithEmailAndPassword(mBinding.unameEdittext.text.toString(), mBinding.pwdEdittext.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful)
                    startActivity(Intent(requireContext(), ProductListActivity::class.java)
                        .putExtra("UID", mAuth.currentUser?.uid))
                else {
                    swapFragment<LoginFragment>(R.id.welcome_fragment_container)
                    Toast.makeText(requireContext(), R.string.bad_login, Toast.LENGTH_LONG).show()
                }
            }

        swapFragment<LoadingFragment>(R.id.welcome_fragment_container)
    }


    private fun onGoToRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        swapFragment<RegisterFragment>(R.id.welcome_fragment_container)
    }


    private inline fun <reified T: Fragment> swapFragment(layoutID: Int) {
        requireActivity().supportFragmentManager.commit {
            replace<T>(layoutID)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }


    class LoadingFragment : Fragment(R.layout.fragment_loading)
}