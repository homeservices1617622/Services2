package com.example.services;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegistrationUser extends AppCompatActivity {

    private Button submit;
    private EditText Uname;
    private EditText USurname;
    private EditText email;
    private EditText password;
    private EditText cpassword;
    private EditText mobilenumber;
    private RadioGroup gender;
    private RadioButton radiobutton;
    private FirebaseAuth mAuth;

    String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseUser mCurrentUser;
    String complete_phone_number;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        binding();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signinwithPhoneAuthcredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(RegistrationUser.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull final PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);


                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {

                                mVerificationId = s;
                                mResendToken = forceResendingToken;
                                Log.e("data", "run: " + mVerificationId);

                            }
                        },
                        10000);
                otpverify();
            }


        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CheakField();


            }
        });
    }

    private void otpverify() {


        final EditText input = new EditText(RegistrationUser.this);
        input.setHint("Enter Otp here");
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        int max = 6;
        InputFilter[] arry = new InputFilter[1];
        arry[0] = new InputFilter.LengthFilter(max);

        input.setFilters(arry);
        final AlertDialog alertDialog = new AlertDialog.Builder(RegistrationUser.this)
                .setTitle("Verify MobileNumber")
                .setMessage("Otp sent sucsessfully ")
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancle", null)

                .setView(input)
                .show();


        Button submit = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = input.getText().toString();
                if (otp.length() != 6) {
                    input.setError("Incorrect Otp");
                } else {
                    Toast.makeText(RegistrationUser.this, "otp is" + otp, Toast.LENGTH_SHORT).show();
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signinwithPhoneAuthcredential(credential);
                alertDialog.dismiss();

            }
        });

        Button cancle = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }

    private void binding() {
        submit = (Button) findViewById(R.id.submit);
        Uname = (EditText) findViewById(R.id.Uname);
        USurname = (EditText) findViewById(R.id.USurname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        cpassword = (EditText) findViewById(R.id.cpassword);
        mobilenumber = (EditText) findViewById(R.id.mobilenumber);
        gender = (RadioGroup) findViewById(R.id.gender);

    }

    private void CheakField() {

        String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
        String MobilePattern = "[0-9]{10}";
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        String Error = "The Item name can not be Empty";
        String name = Uname.getText().toString();
        int selectedId = gender.getCheckedRadioButtonId();
        radiobutton = (RadioButton) findViewById(selectedId);
        String surname = USurname.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String cPassword = cpassword.getText().toString();
        String Mobilenumber = mobilenumber.getText().toString();

        /*if (TextUtils.isEmpty(name)) {

            Uname.setError(Error);

        } else if (TextUtils.isEmpty(surname)) {
            USurname.setError(Error);
        } else if (TextUtils.isEmpty(Email) || !Email.matches(emailPattern)) {
            email.setError("Enter Valid Email Address");
        } else if (TextUtils.isEmpty(Password) || !Password.matches(PASSWORD_PATTERN)) {
            password.setError("Enter Strong Password");

        } else if (TextUtils.isEmpty(cPassword) || !cPassword.matches(Password)) {
            cpassword.setError("Password must be same");
        } else if (selectedId == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Mobilenumber) || !Mobilenumber.matches(MobilePattern)) {
            mobilenumber.setError("Enter Valid Mobile Number");
        } else {


            OtpverifybyFirebase(Mobilenumber);



            Toast.makeText(this, "" + radiobutton.getText(), Toast.LENGTH_SHORT).show();
            String Fullname = name + " " + surname;
            Toast.makeText(this, " " + Fullname, Toast.LENGTH_SHORT).show();


        }*/


        OtpverifybyFirebase(Mobilenumber);

    }

    private void OtpverifybyFirebase(String mobilenumber) {


        complete_phone_number = "+" + 91 + mobilenumber;

        Log.e("data", "OtpverifybyFirebase: " + complete_phone_number);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(complete_phone_number, 60,
                TimeUnit.SECONDS,
                RegistrationUser.this
                , mCallbacks);


    }

    private void signinwithPhoneAuthcredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(RegistrationUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationUser.this, "complate verification", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationUser.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(RegistrationUser.this, "There was an error verifying OTP", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }


}