package com.abhishek.abc.labcalculator;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView bt0;
    private TextView bt1;
    private TextView bt2;
    private TextView bt3;
    private TextView bt4;
    private TextView bt5;
    private TextView bt6;
    private TextView bt7;
    private TextView bt8;
    private TextView bt9;
    private TextView btMul;
    private TextView btAdd;
    private TextView btSub;
    private TextView btDiv;
    private TextView btEqual;
    private TextView btCancel;


    private TextView result;
    private TextView expression;
    private enum POS {
        OPERAND1,
        OPERATOR,
        OPERAND2,
        EQUAL
    }
    private long operand1;
    private long operand2;
    private String operator;
    private POS currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void resetParam() {
        operand1 = 0;
        operand2 = 0;
        operator = null;
        currentPos = POS.OPERAND1;
    }

    public void updateExpression(String s) {
        if(s==null) {
            expression.setText("");
            return;
        }
        if(s.equalsIgnoreCase("=") && expression.getText().toString().endsWith("=")) {
            return;
        }

        try {
            Integer.parseInt(s);
            if ( expression.length() == 1 &&
                    expression.getText().charAt(expression.length()-1) == '0') {
                expression.setText(s);
            } else if (expression.length() > 1 &&
                    expression.getText().charAt(expression.length()-1) == '0' &&
                    !Character.isDigit(expression.getText().charAt(expression.length()-2))) {
                expression.setText(expression.getText().toString().substring(0,expression.getText().length()-1) + s);
            } else {
                expression.setText(expression.getText().toString() + s);
            }
        } catch (Exception e1) {
            try {
                Integer.parseInt(expression.getText().toString().substring(expression.length()-1));
                expression.setText(expression.getText().toString() + s);
            } catch (IndexOutOfBoundsException ix) {
                expression.setText(s);
            }catch (Exception e2) {
                expression.setText(expression.getText().toString().substring(0,expression.getText().length()-1) + s);
            }
        }
    }

    private void init() {
        bt0 = (TextView) findViewById(R.id.bt_0);
        bt0.setOnClickListener(this);
        bt1 = (TextView) findViewById(R.id.bt_1);
        bt1.setOnClickListener(this);
        bt2 = (TextView) findViewById(R.id.bt_2);
        bt2.setOnClickListener(this);
        bt3 = (TextView) findViewById(R.id.bt_3);
        bt3.setOnClickListener(this);
        bt4 = (TextView) findViewById(R.id.bt_4);
        bt4.setOnClickListener(this);
        bt5 = (TextView) findViewById(R.id.bt_5);
        bt5.setOnClickListener(this);
        bt6 = (TextView) findViewById(R.id.bt_6);
        bt6.setOnClickListener(this);
        bt7 = (TextView) findViewById(R.id.bt_7);
        bt7.setOnClickListener(this);
        bt8 = (TextView) findViewById(R.id.bt_8);
        bt8.setOnClickListener(this);
        bt9 = (TextView) findViewById(R.id.bt_9);
        bt9.setOnClickListener(this);
        btMul = (TextView) findViewById(R.id.bt_multiply);
        btMul.setOnClickListener(this);
        btAdd = (TextView) findViewById(R.id.bt_plus);
        btAdd.setOnClickListener(this);
        btSub = (TextView) findViewById(R.id.bt_minus);
        btSub.setOnClickListener(this);
        btDiv = (TextView) findViewById(R.id.bt_divide);
        btDiv.setOnClickListener(this);
        btCancel = (TextView) findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(this);
        btEqual = (TextView) findViewById(R.id.bt_equal);
        btEqual.setOnClickListener(this);

        //Result box
        expression = (TextView) findViewById(R.id.tv_expression);
        result = (TextView) findViewById(R.id.tv_num);
        resetParam();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        operand1 = savedInstanceState.getLong("operand1");
        operand2 = savedInstanceState.getLong("operand2");
        operator = savedInstanceState.getString("operator");
        expression.setText(savedInstanceState.getString("exp"));
        currentPos = POS.values()[savedInstanceState.getInt("pos")];
        result.setText(savedInstanceState.getString("result"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("operand1",operand1);
        outState.putLong("operand2",operand2);
        outState.putString("operator", operator);
        outState.putString("exp",expression.getText().toString());
        outState.putString("result",result.getText().toString());
        outState.putInt("pos",currentPos.ordinal());
        super.onSaveInstanceState(outState);
    }

    public void setResultToDisplay(String war, long num) {
        if (war != null) {
            result.setText(war);
        } else {
            result.setText(String.valueOf(num));
        }
    }

    public long calcResult(long num1, String sym, long num2) throws ArithmeticException {
        if ("*".equalsIgnoreCase(sym)) {
            return num1*num2;
        } else if ("+".equalsIgnoreCase(sym)) {
            return num1+num2;
        }  else if ("-".equalsIgnoreCase(sym)) {
            return num1-num2;
        }  else if ("/".equalsIgnoreCase(sym)) {
            if (sym.equals("/") && num2==0) {
                throw new ArithmeticException();
            }
            return (int) Math.round(((double)num1)/num2);
        } else if ("=".equalsIgnoreCase(sym)) {
            if (num1==num2) {
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }


    public void storeExpressionForSymbol (String sym) {
        if (currentPos == POS.EQUAL) {
            updateExpression(null);
            updateExpression(String.valueOf(operand1));
            operator = sym;
            currentPos = POS.OPERATOR;
        } else if (sym.equals("=") && currentPos == POS.OPERAND2) {
                try {
                    operand1 = calcResult(operand1,operator,operand2);
                    operand2=0;
                    if (operand1>9999999 || operand1<-9999999) {
                        setResultToDisplay("OVERFLOW!",operand1);
                        resetParam();
                        return;
                    }
                    setResultToDisplay(null,operand1);
                } catch (ArithmeticException a) {
                    setResultToDisplay("ERROR!!!",0);
                    resetParam();
                } finally {
                    currentPos = POS.EQUAL;
                }
            } else if ( currentPos == POS.OPERAND1 || currentPos == POS.OPERATOR) {
                operator = sym;

                currentPos = POS.OPERATOR;
            } else {
                try {
                    operand1 = calcResult(operand1,operator,operand2);
                    operand2 = 0;
                    operator = sym;
                    currentPos = POS.OPERATOR;
                } catch (ArithmeticException a) {
                    setResultToDisplay("ERROR!!!",0);
                    resetParam();
                    updateExpression(null);
                }
            }
    }


    public void storeExpressionForDigit (int num) {
        if ( currentPos == POS.EQUAL) {
            resetParam();
            updateExpression(null);
            operand1 = operand1*10 + num;
            setResultToDisplay(null,operand1);
            updateExpression(String.valueOf(num));
        } else if ( currentPos == POS.OPERAND1 ) {
            if ( String.valueOf(operand1).length() < 7 ) {
                operand1 = operand1*10 + num;
                setResultToDisplay(null,operand1);
                updateExpression(String.valueOf(num));
            } else {
                setResultToDisplay("OVERFLOW!",operand1);
                resetParam();
                updateExpression(null);
            }
        } else {
            if ( String.valueOf(operand2).length() < 7 ) {
                currentPos = POS.OPERAND2;
                operand2 = operand2*10 + num;
                setResultToDisplay(null,operand2);
                updateExpression(String.valueOf(num));
            } else {
                setResultToDisplay("OVERFLOW!",operand1);
                resetParam();
                updateExpression(null);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_0:
                storeExpressionForDigit(0);
                break;
            case R.id.bt_1:
                storeExpressionForDigit(1);
                break;
            case R.id.bt_2:
                storeExpressionForDigit(2);
                break;
            case R.id.bt_3:
                storeExpressionForDigit(3);
                break;
            case R.id.bt_4:
                storeExpressionForDigit(4);
                break;
            case R.id.bt_5:
                storeExpressionForDigit(5);
                break;
            case R.id.bt_6:
                storeExpressionForDigit(6);
                break;
            case R.id.bt_7:
                storeExpressionForDigit(7);
                break;
            case R.id.bt_8:
                storeExpressionForDigit(8);
                break;
            case R.id.bt_9:
                storeExpressionForDigit(9);
                break;
            case R.id.bt_multiply:
                storeExpressionForSymbol("*");
                updateExpression("*");
                break;
            case R.id.bt_plus:
                storeExpressionForSymbol("+");
                updateExpression("+");
                break;
            case R.id.bt_minus:
                storeExpressionForSymbol("-");
                updateExpression("-");
                break;
            case R.id.bt_divide:
                storeExpressionForSymbol("/");
                updateExpression("/");
                break;
            case R.id.bt_cancel:
                resetParam();
                setResultToDisplay(null,0);
                updateExpression(null);
                break;
            case R.id.bt_equal:
                storeExpressionForSymbol("=");
                updateExpression("=");
                break;
        }
    }
}
