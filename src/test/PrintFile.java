/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
 
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
 
public class PrintFile {
 
    public static void main(String[] args) throws PrintException, FileNotFoundException {
        InputStream in = new FileInputStream(new File("F:/ap/temp.txt"));
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        AttributeSet attributeSet = new HashAttributeSet();
        attributeSet.add(new PrinterName("NPI8DA48A", null)); 
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
 
        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(in, flavor, null);
        PrintJobWatcher watcher = new PrintJobWatcher(job); 
        job.print(doc, null); 
        watcher.waitForDone(); 
    }
 
    static class PrintJobWatcher {
        // true iff it is safe to close the print job's input stream
        boolean done = false;
 
        PrintJobWatcher(DocPrintJob job) {
            // Add a listener to the print job
            job.addPrintJobListener(new PrintJobAdapter() {
                public void printJobCanceled(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobCompleted(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobFailed(PrintJobEvent pje) {
                    allDone();
                }
                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    allDone();
                }
                void allDone() {
                    synchronized (PrintJobWatcher.this) {
                        done = true;
                        PrintJobWatcher.this.notify();
                    }
                }
            });
        }
 
        public synchronized void waitForDone() {
            try {
                while (!done) {
                    wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}


