package com.example.administrator.arithmetic_master.view;




import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.arithmetic_master.Model.User;
import com.example.administrator.arithmetic_master.R;
import com.example.administrator.arithmetic_master.grade.GradeOne;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CalculateActivity extends FragmentActivity implements CalculateSettingDialog.NoticeDialogListener {


    //计时相关变量
    private Handler timeHandler;
    private TextView exe_time;
    private int totalTime=0;

    //题目相关变量
    private ArrayList<String> contentContainer=new ArrayList<>(); //题目内容保存的容器
    private ArrayList<String> answerContainer=new ArrayList<>(); //答案保存的容器
    private int currentProblemIndex=0;//当前题目索引

    private TextView problem_content;
    private TextView problem_answer;
    private ImageView finishProblem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        showNoticeDialog();
        exe_time=(TextView) findViewById(R.id.exe_time);
        problem_content=(TextView)findViewById(R.id.problem_content);
        problem_answer=(TextView)findViewById(R.id.problem_answer) ;
        finishProblem=(ImageView)findViewById(R.id.img_finishProblem);
        timeHandler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x1233)
                {
                    totalTime++;
                    String hour=(totalTime/3600)+"";
                    String minute=(totalTime-Integer.parseInt(hour)*3600)/60+"";
                    String second=(totalTime%60)+"";
                    String time=hour+":"+minute+":"+second;
                    exe_time.setText(time);
                }
            }
        };

    }

    public void showNoticeDialog()
    {
        CalculateSettingDialog settingDialog=new CalculateSettingDialog();
        settingDialog.show(getSupportFragmentManager(),"CalculateSettingDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        //获取年级，答题数，并将值保存到User中。
        CalculateSettingDialog settingDialog=(CalculateSettingDialog)dialog;

        User.getInstance().currentExeGrade=settingDialog.getGrade();
        User.getInstance().currentExeNum=settingDialog.getNum();

        //根据对话框取得相对应的题目
        for(int i=0;i<User.getInstance().currentExeNum;i++)
        {
            GradeOne gradeOne=new GradeOne();
            contentContainer.add(gradeOne.zhengshi(1,20));
            answerContainer.add("");
        }
        UpdateProblem(currentProblemIndex);

        //开始计时
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                timeHandler.sendEmptyMessage(0x1233);
            }
        },0,1000);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        // User touched the dialog's negative button
        setResult(RESULT_CANCELED,null);
        finish();
    }


    public void NumClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_numOne:
                AlterAnswer("1");
                break;
            case R.id.img_numTwo:
                AlterAnswer("2");
                break;
            case R.id.img_numThree:
                AlterAnswer("3");
                break;
            case R.id.img_numFour:
                AlterAnswer("4");
                break;
            case R.id.img_numFive:
                AlterAnswer("5");
                break;
            case R.id.img_numSix:
                AlterAnswer("6");
                break;
            case R.id.img_numSeven:
                AlterAnswer("7");
                break;
            case R.id.img_numEight:
                AlterAnswer("8");
                break;
            case R.id.img_numNine:
                AlterAnswer("9");
                break;
            case R.id.img_numZero:
                AlterAnswer("0");
                break;
            case R.id.img_clean:
                AlterAnswer("Clean");
                break;
            case R.id.next_problem:
                //防止数组越界
                if(currentProblemIndex<User.getInstance().currentExeNum-1)
                {
                    currentProblemIndex++;
                }

                if(currentProblemIndex==User.getInstance().currentExeNum-1)
                {
                    finishProblem.setVisibility(View.VISIBLE);
                }

                UpdateProblem(currentProblemIndex);
                break;
            case R.id.last_problem:
                if(currentProblemIndex>0)
                {
                    currentProblemIndex--;
                }
                UpdateProblem(currentProblemIndex);
                break;
            case R.id.img_divide:
                AlterAnswer("/");
                break;
            case R.id.img_minus:
                AlterAnswer("-");
                break;
            case R.id.img_point:
                AlterAnswer(".");
                break;
            case R.id.img_return:
                setResult(RESULT_CANCELED,null);
                finish();
                break;
            case R.id.img_finishProblem:
                setResult(RESULT_OK,null);
                finish();
                break;
            default:
                break;
        }
    }

    public void AlterAnswer(String singleChar)
    {
        String answer=answerContainer.get(currentProblemIndex);
        if(singleChar.equals("Clean"))
        {
            if(answer.equals("")){

            }
            else{
                int length=answer.length();
                answer=answer.substring(0,length-1);
            }
        }
        else
        {
            answer=answer+singleChar;
        }
        answerContainer.set(currentProblemIndex,answer);
        UpdateProblem(currentProblemIndex);
    }

    //刷新问题或答案
    private void UpdateProblem(int index)
    {
        problem_content.setText(contentContainer.get(index));
        problem_answer.setText(answerContainer.get(index));
    }
}
