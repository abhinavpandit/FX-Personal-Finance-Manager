/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.panels.register;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import models.Account;
import models.DataModel;
import models.Payee;

/**
 *
 * @author HP
 */
public class FilterEntriesPopUp extends Popup
{
    private GridPane mainGrid;
    private DatePicker fromDate;
    private DatePicker toDate;
    private final DataModel dataModel;
    private ChoiceBox<Account> fromAcChoiceBox;
    private ChoiceBox<Account> toAcChoiceBox;
    private ChoiceBox<Payee> payeeChoiceBox;
    private TextField memoField;
    private TextField greaterThanTextField;
    private TextField lessThanTextField;
    private Button filterButton;
    
    public FilterEntriesPopUp(DataModel dataModel)
    {
        super();
        this.dataModel = dataModel;
        
        setUpMainGrid();
        
    }

    private void setUpMainGrid() 
    {
        mainGrid = new GridPane();
        mainGrid.setVgap(5);
        mainGrid.setHgap(5);
        
        ColumnConstraints col1 = new ColumnConstraints(200);
        ColumnConstraints col2 = new ColumnConstraints(200);
        mainGrid.getColumnConstraints().addAll(col1,col2);
        
        Label fromDateLabel = new Label("FROM DATE");
        Label toDateLabel = new Label("TO DATE");
        
        fromDate = new DatePicker(LocalDate.MIN);
        toDate = new DatePicker(LocalDate.MAX);
        
        Label fromAcLabel = new Label("From Account");
        fromAcChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        
        Label toAcLabel = new Label("To Label");
        toAcChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        
        Label payeeLabel = new Label("PAYEE");
        payeeChoiceBox = new ChoiceBox<>(dataModel.getPayeeList());
        
        Label memoLabel = new Label("MEMO");
        memoField = new TextField();
        
        Label greaterThanLabel = new Label("Greater Than : > ");
        greaterThanTextField = new TextField();
        
        Label lessThanLabel = new Label("Less Than : < ");
        lessThanTextField = new TextField();
        
        filterButton = new Button("FILTER");
        
        
        mainGrid.add(fromDateLabel, 0, 0);
        mainGrid.add(toDateLabel,1,0);
        
        mainGrid.add(fromDate,0,1);
        mainGrid.add(toDate,1,1);
        
        mainGrid.add(fromAcLabel,0,2);
        mainGrid.add(toAcLabel,1,2);
        
        mainGrid.add(fromAcChoiceBox,0,3);
        mainGrid.add(toAcChoiceBox, 1, 3);
        
        mainGrid.add(payeeLabel,0,4);
        mainGrid.add(payeeChoiceBox,1,4);
        
        mainGrid.add(memoLabel,0,5);
        mainGrid.add(memoField,1,5);
        
        mainGrid.add(greaterThanLabel, 0, 6);
        mainGrid.add(greaterThanTextField,1,6);
        
        mainGrid.add(lessThanLabel, 0, 7);
        mainGrid.add(lessThanTextField ,1,7);
        
        mainGrid.add(filterButton,0,8);
        
    }
    
    
}
