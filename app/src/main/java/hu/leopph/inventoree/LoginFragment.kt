package hu.leopph.inventoree


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val m_Binding: FragmentLoginBinding get() = _mBinding!!


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
        m_Binding.loginButton.setOnClickListener(this::onLoginPressed)
        return m_Binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


    fun onLoginPressed(view: View) {
        if (m_Binding.unameEdittext.text.toString().isBlank() || m_Binding.unameEdittext.text.toString().isBlank())
            return

        requireActivity().supportFragmentManager.commit {
            replace<LoadingFragment>(R.id.welcome_fragment_container)
            setReorderingAllowed(true)
            addToBackStack(null)
        }

        mAuth.signInWithEmailAndPassword(m_Binding.unameEdittext.text.toString(), m_Binding.pwdEdittext.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful)
                    println("pereg az epic")
                else
                    println("eef")
            }
    }


    class LoadingFragment : Fragment(R.layout.fragment_loading)
}