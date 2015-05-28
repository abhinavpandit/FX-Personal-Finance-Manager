package ui.panels.contentPanes;

import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import models.DataModel;
import ui.panels.charts.ChartPane;
import ui.panels.register.RegisterPanel;

import ui.panels.reportPanel.ReportPanelWebTest;

import ui.panels.summary.SummaryPanel;

public class ContentPane 
{
	private TabPane contentPane;
	private Tab summaryTab;
	private Tab registerTab;
	private Tab chartsTab;
	private Tab reportsTab;
        
	private RegisterPanel registerPanel;
	private DataModel dataModel;
	private StringProperty status;
	private SummaryPanel summaryPanel;
	
	public ContentPane(DataModel dataModel, StringProperty status) throws IOException
	{
		this.dataModel = dataModel;
		this.status = status;
		
	    contentPane = new TabPane();
	    contentPane.tabClosingPolicyProperty().set(TabClosingPolicy.UNAVAILABLE);
	    contentPane.setSide(Side.TOP);
            contentPane.getStylesheets().add(this.getClass().getResource("contentpane.css").toExternalForm());
	    
	    registerPanel = new RegisterPanel(dataModel,status);
	    registerTab = new Tab("REGISTER VIEW");
	    registerTab.setContent(registerPanel.getRegisterPanel());
	    registerPanel.setStatusProperty(status);
	     
	    summaryPanel = new SummaryPanel(dataModel);
	    summaryTab = new Tab("SUMMARY");
            ImageView summaryIcon = new ImageView(this.getClass().getResource("summary.png").toExternalForm());
            summaryIcon.setFitHeight(16);summaryIcon.setFitWidth(16);
            summaryTab.setGraphic(summaryIcon);
	    summaryTab.setContent(summaryPanel.getMainGrid());
	    
	    chartsTab = new Tab("Charts");
            ChartPane chartPane = new ChartPane(dataModel,status);
            ImageView chartsIcon = new ImageView(this.getClass().getResource("charts.png").toExternalForm());
            chartsIcon.setFitHeight(16);chartsIcon.setFitWidth(16);
            chartsTab.setGraphic(chartsIcon);
            chartsTab.setContent(chartPane);
            
	   
            
            reportsTab = new Tab("Reports");
            ImageView reportsIcon = new ImageView(this.getClass().getResource("report.png").toExternalForm());
            reportsIcon.setFitHeight(16);reportsIcon.setFitWidth(16);
            reportsTab.setGraphic(reportsIcon);
          
            reportsTab.setContent(new ReportPanelWebTest(dataModel));
            
	    
	     
	    contentPane.getTabs().addAll(summaryTab,registerTab,chartsTab,reportsTab);
           
	    //contentPane.setStyle("-fx-font-family: 'Helvetica';-fx-font-weight: bold;");
	  
	    contentPane.setMinHeight(500);
            contentPane.setMaxHeight(Double.MAX_VALUE);
            
            contentPane.setMaxHeight(Double.MAX_VALUE);
	}
	
	public TabPane getTabPane()
	{
		return contentPane;
	}

}
