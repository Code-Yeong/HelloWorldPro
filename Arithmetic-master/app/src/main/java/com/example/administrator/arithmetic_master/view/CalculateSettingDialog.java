package com.example.administrator.arithmetic_master.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.administrator.arithmetic_master.Model.User;
import com.example.administrator.arithmetic_master.R;

/**
 * Created by Administrator on 2016/10/15.
 */
public class CalculateSettingDialog extends DialogFragment {

    private int grade;
    private int num;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater:获取自定义的Layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout_casSetting = inflater.inflate(R.layout.calsetting,null);
        AdapterDialogElement(layout_casSetting);

        //将自定义的Layout设置为Dialog的样式
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(casSetting);
        builder.setView(layout_casSetting)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(CalculateSettingDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(CalculateSettingDialog.this);
                    }
                });
        return builder.create();
    }

    //在拥有该对话框的Activity中实现该接口，可以获得对话框的消息
    public interface NoticeDialogListener
    {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        }
        catch (ClassCastException e)
        {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    //适配对话框上面的两个Spinner
    public void AdapterDialogElement(View casSetting)
    {
        Spinner sp_grade = (Spinner) casSetting.findViewById(R.id.sp_grade);
        Spinner sp_questionNum=(Spinner) casSetting.findViewById(R.id.sp_num);


        //设置年级的Adapter
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> gradeAdapter = ArrayAdapter.createFromResource(getContext(),R.array.grade_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //设置问题数的Adapter
        ArrayAdapter<CharSequence> questionNumAdapter = ArrayAdapter.createFromResource(getContext(),R.array.question_num_array, android.R.layout.simple_spinner_item);
        questionNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grade=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_questionNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num=Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Apply the adapter to the spinner
        sp_grade.setAdapter(gradeAdapter);
        sp_questionNum.setAdapter(questionNumAdapter);
    }

    //获得年级
    public int getGrade()
    {
        return  grade;
    }

    //获得题目数量
    public int getNum()
    {
        return num;
    }
}
