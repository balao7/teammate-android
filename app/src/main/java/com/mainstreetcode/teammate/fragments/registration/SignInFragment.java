package com.mainstreetcode.teammate.fragments.registration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.ViewCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mainstreetcode.teammate.R;
import com.mainstreetcode.teammate.activities.RegistrationActivity;
import com.mainstreetcode.teammate.baseclasses.RegistrationActivityFragment;
import com.mainstreetcode.teammate.util.ErrorHandler;
import com.tunjid.androidbootstrap.core.abstractclasses.BaseFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;


public final class SignInFragment extends RegistrationActivityFragment
        implements
        TextView.OnEditorActionListener {

    private EditText emailInput;
    private EditText passwordInput;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        fragment.setEnterExitTransitions();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        View border = rootView.findViewById(R.id.card_view_wrapper);
        emailInput = rootView.findViewById(R.id.email);
        passwordInput = rootView.findViewById(R.id.password);

        rootView.findViewById(R.id.forgot_password).setOnClickListener(this);
        passwordInput.setOnEditorActionListener(this);

        ViewCompat.setTransitionName(emailInput, SplashFragment.TRANSITION_TITLE);
        ViewCompat.setTransitionName(passwordInput, SplashFragment.TRANSITION_SUBTITLE);
        ViewCompat.setTransitionName(border, SplashFragment.TRANSITION_BACKGROUND);

        disposables.add(Completable.timer(200, TimeUnit.MILLISECONDS).observeOn(mainThread()).subscribe(() -> {
            rootView.findViewById(R.id.email_wrapper).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.password_wrapper).setVisibility(View.VISIBLE);
        }, ErrorHandler.EMPTY));

        return rootView;
    }

    @Override
    public void togglePersistentUi() {
        updateFabIcon();
        setFabClickListener(this);
        setToolbarTitle(getString(R.string.sign_in));
        super.togglePersistentUi();
    }

    @Override
    @StringRes
    protected int getFabStringResource() { return R.string.submit; }

    @Override
    @DrawableRes
    protected int getFabIconResource() { return R.drawable.ic_check_white_24dp; }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        emailInput = null;
        passwordInput = null;
    }

    @Override
    public boolean showsFab() {
        return true;
    }

    @Nullable
    @Override
    @SuppressLint("CommitTransaction")
    public FragmentTransaction provideFragmentTransaction(BaseFragment fragmentTo) {
        if (getView() != null && fragmentTo.getStableTag().contains(ForgotPasswordFragment.class.getSimpleName())) {
            return beginTransaction()
                    .addSharedElement(emailInput, SplashFragment.TRANSITION_TITLE)
                    .addSharedElement(getView().findViewById(R.id.card_view_wrapper), SplashFragment.TRANSITION_BACKGROUND);
        }
        return super.provideFragmentTransaction(fragmentTo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                signIn();
                break;
            case R.id.forgot_password:
                showFragment(ForgotPasswordFragment.newInstance(emailInput.getText()));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                && (event.getAction() == KeyEvent.ACTION_DOWN))) {
            signIn();
            return true;
        }
        return false;
    }

    private void signIn() {
        if (VALIDATOR.isValidEmail(emailInput) && VALIDATOR.isValidPassword(passwordInput)) {
            toggleProgress(true);

            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            disposables.add(viewModel.signIn(email, password)
                    .subscribe(user -> onSignIn(), defaultErrorHandler)
            );
        }
    }

    private void onSignIn() {
        toggleProgress(false);
        hideKeyboard();
        RegistrationActivity.startMainActivity(getActivity());
    }
}
