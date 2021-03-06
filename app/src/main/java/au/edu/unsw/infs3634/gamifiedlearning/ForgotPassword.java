package au.edu.unsw.infs3634.gamifiedlearning;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatDialogFragment;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatDialogFragment {
    private FirebaseAuth firebaseAuth;
    private EditText send_email;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // build a new dialog for forgot password.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        //show activity forgot password xml
        View view = inflater.inflate(R.layout.activity_forgot_password,null);

        send_email = view.findViewById(R.id.tv_email);
        firebaseAuth = FirebaseAuth.getInstance();


        // modify the dialog
        //set two buttons's action when it was pressed
        builder.setView(view);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // when cancel was clicked, then the dialog close and do nothing
                dialog.cancel();
            }
        });
        //modify the send button
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String sendemail = (String) send_email.getText().toString().trim();

                // email textfiled can not be null, otherwise popup relate message

                if (!TextUtils.isEmpty(sendemail)) {

                    try {
                        firebaseAuth.sendPasswordResetEmail(sendemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // if the email was correct,send a link to users email to reset password
                                //the dialog closed, user will be back to login page
                                if (task.isSuccessful()) {
                                    Toast.makeText(view.getContext(), "Please check your email", Toast.LENGTH_SHORT).show();
                                    try{startActivity(new Intent(view.getContext(), Login.class));}
                                    catch (Exception e) {
                                        System.out.println("Exception captured");
                                    }
                                } else {
                                    //otherwise an error exception catched
                                    Toast.makeText(view.getContext(), "Email is incorrect,please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //catch eceeption when there is nothing entered
                    } catch (Exception e) {
                        System.out.println("yoo this user did not enter the required fields");
                        Toast.makeText(view.getContext(), "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(view.getContext(), "All fileds are required", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return builder.create();
    }
}