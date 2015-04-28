/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.commons;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author john
 */
public class StatusPanel extends HBox
{
    private StringProperty status;
    private Label label;
    
    public StatusPanel(StringProperty status)
    {
        this.status = status;
        label = new Label();
        status.bindBidirectional(label.textProperty());
        this.getChildren().add(label);
        this.setPrefHeight(20);
        this.setMinHeight(20);
        label.setStyle("-fx-font-weight: bold;-fx-font-family:'Helvetica';-fx-font-size: 11;");
        this.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        
    }
    
    public StringProperty statusProperty()
    {
        System.out.println("status panel is returnin : "+status);
        return status;
       
    }
    
}
