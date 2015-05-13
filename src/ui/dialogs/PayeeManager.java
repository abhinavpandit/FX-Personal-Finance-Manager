package ui.dialogs;


import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.DataModel;
import models.Payee;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Transaction;



public class PayeeManager extends Application 
{
	private Stage primaryStage;
	private Scene scene;
	private VBox root;
	private DataModel dataModel;
	private ListView<Payee> payeeListView;
	private HBox buttonPanel;
	private TextField newPayeeField;
	private Button addPayeeButton;
	private Button updatePayeeButton;
        private Button deletePayeeButton;
	private Label topLabel = new Label("PAYEE MANAGER");
	private Label messageLabel = new Label();
	private ObservableList<Payee> payeeList;
	
	public  PayeeManager() throws SQLException 
	{
            //dataModel = new DataModel(new SimpleStringProperty());
	}
	public  PayeeManager(DataModel dataModel) throws SQLException 
	{
		this.dataModel = dataModel;
	}
	

	@Override
	public void start(Stage mainStage) throws Exception
	{
		
        primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(mainStage);
        
       
        setUpUI();
        wireUpControls();
        scene = new Scene(root,400,400,Color.ALICEBLUE);
        primaryStage.setScene(scene);
        scene.setFill(Color.AQUA);
        root.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setTitle("Payee Manager");
        primaryStage.setResizable(false);
        primaryStage.show();
		
	}
        private void setUpUI()
        {
             root = new VBox(10);
             root.setPadding(new Insets(5));
        root.getChildren().add(topLabel);
        topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        topLabel.setAlignment(Pos.CENTER);
            ImageView payeeIcon = new ImageView(this.getClass().getResource("res/payee.png").toExternalForm());
            payeeIcon.setFitHeight(48);
            payeeIcon.setFitWidth(48);
        topLabel.setGraphic(payeeIcon);
        VBox.setMargin(topLabel, new Insets(5, 20, 5, 20));
        
        payeeListView = new ListView<Payee>();
        payeeListView.setMaxHeight(300);
        
        payeeListView.setItems(dataModel.getPayeeList());
        payeeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        newPayeeField = new TextField();
        newPayeeField.setPromptText("Enter Payee Name Here");
        newPayeeField.setEditable(true);
        addPayeeButton = new Button("ADD");
        updatePayeeButton = new Button("UPDATE");
        deletePayeeButton = new Button("DELETE");
        buttonPanel = new HBox(20);
        buttonPanel.getChildren().addAll(newPayeeField,addPayeeButton,updatePayeeButton,deletePayeeButton);
        
        messageLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        root.getChildren().addAll(payeeListView,buttonPanel,messageLabel);
        }
	private void wireUpControls()
	{
        //newPayeeField.textProperty().bind(payeeListView.getSelectionModel().selectedItemProperty().asString());
        addPayeeButton.setOnAction(event -> {  
        if(newPayeeField.getText().length()<3)
        {
                messageLabel.setText("Payee Name should be atleast 3 Characters");
                return;
        }
        Payee p = new Payee(newPayeeField.getText());
        try 
        {
            boolean added = dataModel.addPayee(p);
            if(added == true)
            {
                  //  payeeList.add(p);
                    messageLabel.setText("Payee : { " +p.getPayeeID() +" , "  +p.getName() +" } successfuly ADDED");
            }
            else
            {
                    messageLabel.setText("ERROR in Adding Payee");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }


        });
		
        updatePayeeButton.setOnAction(event -> { 

        Payee p = payeeListView.getSelectionModel().getSelectedItem();
        if(newPayeeField.getText().length()<3)
        {
                messageLabel.setText("Payee Name should be atleast 3 Characters");
                return;
        }
        int selectedIndex = payeeListView.getSelectionModel().getSelectedIndex();
        if(p == null)
        {
                messageLabel.setText("Select a Payee from the List to Update");
                return;
        }
        if(p!=null)
        {
            try 
            {

              boolean updated =  dataModel.updatePayee(p,newPayeeField.getText());
              if(updated == false)
              {
                      messageLabel.setText("Payee : { " +p.getPayeeID() +" , "  +p.getName() +" } could not be UPDATED");
              }
              else
              {
                      payeeList.set(selectedIndex, p);
                      messageLabel.setText("Payee : { " +p.getPayeeID() +" , "  +p.getName() +" } successfuly UPDATED");
              }
            }
            catch (Exception e) 
            {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }
        });
            payeeListView.setOnMouseClicked(event -> {  
            if(event.getClickCount() == 2)
                this.newPayeeField.setText(payeeListView.getSelectionModel().getSelectedItem().getName());
            
            });
            
            deletePayeeButton.setOnAction(event -> { 
            
                if(payeeListView.getSelectionModel().getSelectedItem() == null)
                {
                    messageLabel.setText("SELECT A PAYEE TO BE DELETED");
                    return;
                }
                Payee p = payeeListView.getSelectionModel().getSelectedItem();
                
                int payeeTransactionCount = 0;
                for(int i=0;i<dataModel.getTransactionList().size();i++)
                {
                    Transaction t = dataModel.getTransactionList().get(i);
                    if(t.getPayee() == p)
                    {
                        payeeTransactionCount++;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Delete Payee");
                alert.setHeaderText("Payee : "+p.getName() +" has associated Transactions");
                
                alert.setContentText("Deleting this Payee will update " +payeeTransactionCount  +" transactions associated with this payee\nDo you want to proceed ?");
                alert.getButtonTypes().add(ButtonType.CANCEL);
                
                 Optional<ButtonType> showAndWait = alert.showAndWait();
                 if(showAndWait.get().equals(ButtonType.OK))
                 {
                     updateTransactionsBeforeRemovingPayee(p);
                     try 
                         {
                            // System.out.println("PAYEEMANAGER : call() to delete payee : "+p.getName() +" > "+Thread.currentThread().getName());
                             dataModel.deletePayee(p);
                         } 
                         catch (SQLException ex) 
                         {
                             Logger.getLogger(PayeeManager.class.getName()).log(Level.SEVERE, null, ex);
                         }
                 }
                
            
            });
	}
        private void updateTransactionsBeforeRemovingPayee(Payee p)
        {
            for(int i=0;i<dataModel.getTransactionList().size();i++)
            {
                Transaction t = dataModel.getTransactionList().get(i);
                System.out.println("at i = "+i +"PayeeManager : iteration : "+t.getMemo() +" >> "+Thread.currentThread().getName());
                if(t.getPayee() == p)
                {
                    System.out.println("at i =" +i +" PayeeManager : successful iteration : "+t.getMemo());
                   Transaction tnew = Transaction.createTransaction(t.getFromAC(), t.getToAC());
                   tnew.setAmount(t.getAmount());
                   tnew.setMemo(t.getMemo());
                   tnew.setPayee(null);
                   tnew.setTransactionDate(t.getTransactionDate());
                   tnew.setTransactionNum(t.getTransactionNum());
                   System.out.println("PayeeManager: calling update on Transaction: "+t.getTransactionID());

                    try 
                    {
                        dataModel.updateTransaction(tnew, t);
                    } catch (SQLException ex) {
                        Logger.getLogger(PayeeManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        System.out.println("PayeeManager : call to update Transaction Finished");  
                }
            }
        }
	public static void main(String[] args)
	{
		launch(args);

	}

}
