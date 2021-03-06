package com.example.administrator.arithmetic_master.view;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.arithmetic_master.Model.User;
import com.example.administrator.arithmetic_master.R;
import com.example.administrator.arithmetic_master.base.BaseGenerator;
import com.example.administrator.arithmetic_master.baseimpl.BaseGeneratorImpl;
import com.example.administrator.arithmetic_master.grade.GradeFive;
import com.example.administrator.arithmetic_master.grade.GradeFour;
import com.example.administrator.arithmetic_master.grade.GradeOne;
import com.example.administrator.arithmetic_master.grade.GradeSix;
import com.example.administrator.arithmetic_master.grade.GradeThree;
import com.example.administrator.arithmetic_master.grade.GradeTwo;
import com.example.administrator.arithmetic_master.http.HttpUtil;
import com.example.administrator.arithmetic_master.utils.Convert;
import com.example.administrator.arithmetic_master.utils.DisplayMsg;
import com.example.administrator.arithmetic_master.utils.EncodeAndDecode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CalculateActivity extends FragmentActivity implements CalculateSettingDialog.NoticeDialogListener {


    //计时相关变量
    private Handler timeHandler;
    private TextView exe_time;
    private int totalTime = 0;

    //题目相关变量
    private ArrayList<String> contentContainer = new ArrayList<>(); //题目内容保存的容器
    private ArrayList<String> answerContainer = new ArrayList<>(); //答案保存的容器
    private ArrayList<String> correctAnswerContainer = new ArrayList<>();//正确答案的容器
    private int currentProblemIndex = 0;//当前题目索引

    private Button btn_last, btn_next;
    private TextView problem_content;
    private TextView problem_answer;
    private ImageView finishProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        showNoticeDialog();
        exe_time = (TextView) findViewById(R.id.exe_time);
        problem_content = (TextView) findViewById(R.id.problem_content);
        problem_answer = (TextView) findViewById(R.id.problem_answer);
        finishProblem = (ImageView) findViewById(R.id.img_finishProblem);

        btn_last = (Button) findViewById(R.id.last_problem);
        btn_next = (Button) findViewById(R.id.next_problem);
        btn_last.setVisibility(View.INVISIBLE);

        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x1233) {
                    totalTime++;
                    String hour = (totalTime / 3600) + "";
                    String minute = (totalTime - Integer.parseInt(hour) * 3600) / 60 + "";
                    String second = (totalTime % 60) + "";
                    String time = hour + ":" + minute + ":" + second;
                    exe_time.setText(time);
                }
            }
        };

    }

    public void showNoticeDialog() {
        CalculateSettingDialog settingDialog = new CalculateSettingDialog();
        settingDialog.show(getSupportFragmentManager(), "CalculateSettingDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //获取年级，答题数，并将值保存到User中。
        CalculateSettingDialog settingDialog = (CalculateSettingDialog) dialog;

        User.getInstance().currentExeGrade = settingDialog.getGrade();
        User.getInstance().currentExeNum = settingDialog.getNum();

        //根据对话框内容取得相对应的题目、正确答案及用户默认计算结果，默认结果为空字符串
        for (int i = 0; i < User.getInstance().currentExeNum; i++) {
            //生成题目
            String problem = GenerateProblem(User.getInstance().currentExeGrade);
            contentContainer.add(problem);
            //生成对应题目的正确答案。
            BaseGenerator baseGenerator = new BaseGeneratorImpl();
            String correctAsn = baseGenerator.calculate(Convert.infix2postfixMdf(problem));
            Log.i("answer", correctAsn);
            correctAnswerContainer.add(correctAsn);
            //初始化用户计算结棍容器
            answerContainer.add("");
        }
        UpdateProblem(currentProblemIndex);

        //开始计时
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                timeHandler.sendEmptyMessage(0x1233);
            }
        }, 0, 1000);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        setResult(RESULT_CANCELED, null);
        finish();
    }


    public void NumClick(View view) {
        if (currentProblemIndex == User.getInstance().currentExeNum - 1) {
            finishProblem.setVisibility(View.VISIBLE);
        }
        switch (view.getId()) {
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
                btn_last.setVisibility(View.VISIBLE);
                //防止数组越界
                if (currentProblemIndex < User.getInstance().currentExeNum - 1) {
                    currentProblemIndex++;
                }

                if (currentProblemIndex == User.getInstance().currentExeNum - 1) {
//                    finishProblem.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.INVISIBLE);
                }

                UpdateProblem(currentProblemIndex);
                break;
            case R.id.last_problem:
                if (currentProblemIndex > 0) {
                    currentProblemIndex--;
                    btn_next.setVisibility(View.VISIBLE);
                } else {
                    btn_last.setVisibility(View.INVISIBLE);
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
                finish();
                break;
            case R.id.img_finishProblem:
                SendResult();
                finish();
                break;
            default:
                break;
        }
    }

    //修改答案
    public void AlterAnswer(String singleChar) {
        String answer = answerContainer.get(currentProblemIndex);
        if (singleChar.equals("Clean")) {
            if (answer.equals("")) {

            } else {
                int length = answer.length();
                answer = answer.substring(0, length - 1);
            }
        } else {
            answer = answer + singleChar;
        }
        answerContainer.set(currentProblemIndex, answer);
        UpdateProblem(currentProblemIndex);
    }

    //刷新问题或答案
    private void UpdateProblem(int index) {
        problem_content.setText(contentContainer.get(index));
        problem_answer.setText(answerContainer.get(index));
    }

    public String GenerateProblem(int grade) {
        String problem = "";
        switch (grade) {
            case 1:
                GradeOne gradeOne = new GradeOne();
                problem = gradeOne.getIntergralExpression();
                break;
            case 2:
                GradeTwo gradeTwo = new GradeTwo();
                problem = gradeTwo.getIntergralExpression();
                break;
            case 3:
                GradeThree gradeThree = new GradeThree();
                problem = gradeThree.getIntergralExpression();
                break;
            case 4:
                GradeFour gradeFour = new GradeFour();
                problem = gradeFour.getIntergralExpression();
                break;
            case 5:
                GradeFive gradeFive = new GradeFive();
                problem = gradeFive.getIntergralExpression();
                break;
            case 6:
                GradeSix gradeSix = new GradeSix();
                problem = gradeSix.getIntergralExpression();
                break;
            default:
                DisplayMsg.Show(this, "年级错误");
                break;
        }
        return problem;
    }

    private void SendResult() {
        String wrongProblem = "";
        String userId = User.getInstance().getUserId();
        String totalCount = User.getInstance().currentExeNum + "";
        String useTime = totalTime + "";
        int rightCount = 0;
        for (int i = 0; i < User.getInstance().currentExeNum; i++) {
            if (answerContainer.get(i).equals(correctAnswerContainer.get(i))) {  //增加正确的题目数量
                rightCount++;
            } else {  //记录错误的题目
                wrongProblem += contentContainer.get(i) + "|";
            }
        }
        String str_rightCount = rightCount + "";
        wrongProblem = wrongProblem.substring(0, wrongProblem.length() - 1);

        //计算分数
        float score = 0;
        float float_rightCount = rightCount;
        float float_totalCount = User.getInstance().currentExeNum;

        if (User.getInstance().getUserGrade() < 4) {
            score = (float_rightCount / float_totalCount) * (5 * float_totalCount / totalTime);
        } else {
            score = (float_rightCount / float_totalCount) * (10 * float_totalCount / totalTime);
        }
        DisplayMsg.Show(this, "您答对了" + str_rightCount + ",所得积分为：" + score);

        //将记录发送到服务端记录
        String url = "recordsAction_saveRecord.action?userid=" + userId + "&rightcount=" + str_rightCount + "&totalcount=" + totalCount + "&time=" + useTime;
        new HttpUtil(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        }, url).start();

        //将错误题目发送到服务端
        Log.i("Wrong Problem......", wrongProblem);
        String newEncodeWrongProblem = EncodeAndDecode.Encode(wrongProblem);
        Log.i("Wrong Problem New...", newEncodeWrongProblem);
        String wrongProblemUrl = "errorsAction_saveError.action?userid=" + userId + "&content=" + newEncodeWrongProblem;
        new HttpUtil(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        }, wrongProblemUrl).start();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
