package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ActionListenerFrame extends JFrame implements ActionListener
{
    JLabel number1,number2,result;
    JTextField txtnumber1,txtnumber2,txtresult;
    JButton buttonAdd,substract,multiply,divide;
	JPanel panel = new JPanel();
    ActionListenerFrame()
    {
         JFrame frame=new JFrame();
        panel.setLayout(new FlowLayout());
        number1=new JLabel("Number1:");
        txtnumber1=new JTextField(10);
        number2=new JLabel("Number2:");
        txtnumber2=new JTextField(10);
        result=new JLabel("Result:");
        txtresult=new JTextField(10);
        //creatio of the buttons..
        buttonAdd=new JButton("Add");
        substract=new JButton("Subtract");
        multiply=new JButton("Multiply");
        divide=new JButton("Divide");
        //Resister the current object as event listener for button
        buttonAdd.addActionListener(this);
        substract.addActionListener(this);
        multiply.addActionListener(this);
        divide.addActionListener(this);
        panel.add(number1);
        panel.add(txtnumber1);
        panel.add(number2);
        panel.add(txtnumber2);
        panel.add(result);
        panel.add(txtresult);
        panel.add(buttonAdd);
        panel.add(substract);
        panel.add(multiply);
        panel.add(divide);


        this.add(panel);
    }
    public void actionPerformed(ActionEvent e)
    {
		double num1 = Double.parseDouble(txtnumber1.getText());
		double num2 = Double.parseDouble(txtnumber2.getText());
        if(e.getSource()==buttonAdd)
        {
        	
           txtresult.setText("" +(num1+num2));
        }
        else if(e.getSource()==substract)
        {
            txtresult.setText("" +(num1-num2));
        }
        else if(e.getSource()==multiply)
        {
           txtresult.setText("" +(num1*num2));
        }
        else
        {
            txtresult.setText("" +(num1/num2));
        }
    }
}
public class NewClass13 
{
    public static void main(String[] args)
    {
        ActionListenerFrame frame=new ActionListenerFrame();
        frame.setTitle("button functioning");
        frame.setSize(250, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
