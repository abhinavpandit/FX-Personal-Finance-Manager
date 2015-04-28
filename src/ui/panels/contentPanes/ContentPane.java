package ui.panels.contentPanes;

import javafx.beans.property.StringProperty;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import models.DataModel;
import ui.panels.charts.ChartPane;
import ui.panels.register.RegisterPanel;
import ui.panels.reportPanel.ReportPanel;
import ui.panels.reportPanel.ReportPanelWebView;
import ui.panels.summary.SummaryPanel;

public class ContentPane 
{
	private TabPane contentPane;
	private Tab summaryTab;
	private Tab registerTab;
	private Tab chartsTab;
	private Tab reportsTab;
        private Tab reportsHTMLtab;
	private RegisterPanel registerPanel;
	private DataModel dataModel;
	private StringProperty status;
	private SummaryPanel summaryPanel;
	
	public ContentPane(DataModel dataModel, StringProperty status)
	{
		this.dataModel = dataModel;
		this.status = status;
		
		contentPane = new TabPane();
	    contentPane.tabClosingPolicyProperty().set(TabClosingPolicy.UNAVAILABLE);
	    contentPane.setSide(Side.LEFT);
            contentPane.getStylesheets().add(this.getClass().getResource("contentpane.css").toExternalForm());
	    
	    registerPanel = new RegisterPanel(dataModel,status);
	    registerTab = new Tab("REGISTER VIEW");
	    registerTab.setContent(registerPanel.getRegisterPanel());
	    registerPanel.setStatusProperty(status);
	     
	    summaryPanel = new SummaryPanel(dataModel);
	    summaryTab = new Tab("SUMMARY");
	    summaryTab.setContent(summaryPanel.getMainGrid());
	    
	    chartsTab = new Tab("Charts");
            ChartPane chartPane = new ChartPane(dataModel,status);
            chartsTab.setContent(chartPane);
            
	    reportsTab =  new Tab("Reports");
            reportsHTMLtab = new Tab("HTML Reports");
            
            ReportPanel reportPanel = new ReportPanel(dataModel);
            reportsTab.setContent(reportPanel);
            
            reportsHTMLtab.setContent(new ReportPanelWebView(dataModel));
            
	    
	     
	    contentPane.getTabs().addAll(summaryTab,registerTab,chartsTab,reportsTab);
            contentPane.getTabs().add(reportsHTMLtab);
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
