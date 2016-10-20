package com.example.administrator.arithmetic_master.grade;


import com.example.administrator.arithmetic_master.baseimpl.BaseGeneratorImpl;

import java.util.Random;

public class GradeOne extends BaseGeneratorImpl {
	public String getIntergralExpression() {
        String reg;
        double d = new Random().nextDouble();
        if (d <= 0.3) {
            reg = zhengshi(0, 40);
        } else if (d > 0.3 && d <= 0.6) {
            reg = zhengshi(0, 20) + "+" + zhengshu(0, 20);
        } else {
            reg = zhengshu(0, 20) + "+" + zhengshi(0, 20);
        }
        return reg;
    }
}
