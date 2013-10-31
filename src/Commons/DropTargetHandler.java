/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2013 Benoit Aigouy 

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are
 met:

 (1) Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer. 

 (2) Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in
 the documentation and/or other materials provided with the
 distribution.  
    
 (3)The name of the author may not be used to
 endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */
package Commons;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

/**
 * DropTargetHandler handles files DnD
 *
 * @author Benoit Aigouy
 */
public class DropTargetHandler implements DropTargetListener {

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable t = dtde.getTransferable();
            DataFlavor[] flavors = null;
            try {
                flavors = t.getTransferDataFlavors();
            } catch (Exception e) {
            }
            if (flavors != null) {
                for (DataFlavor dataFlavor : flavors) {
                    if (dataFlavor.isFlavorJavaFileListType()) { //windows
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        java.util.List list = (java.util.List) t.getTransferData(DataFlavor.javaFileListFlavor);
                        for (int j = 0; j < list.size(); j++) {
                            String data = list.get(j) + "";
                            doSomethingWithTheFile(data);
                        }
                        dtde.dropComplete(true);
                        return;
                    } else if (dataFlavor.isFlavorTextType()) { //linux ubuntu only
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        String data = ((String) (t.getTransferData(DataFlavor.stringFlavor)));
                        ArrayList<String> file_list = CommonClassesLight.UriListToStringArray(data); //create an array out of a single string by parsing it
                        if (!file_list.isEmpty()) {
                            for (int j = 0; j < file_list.size(); j++) {
                                data = file_list.get(j);
                                doSomethingWithTheFile(data);
                            }
                        }
                        dtde.dropComplete(true);
                        return;
                    } else if (dataFlavor.isRepresentationClassInputStream()) { //kubuntu only
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        String data = ((String) (t.getTransferData(DataFlavor.stringFlavor)));
                        String[] result = data.split("\n");
                        if (result != null) {
                            for (int j = 0; j < result.length; j++) {
                                data = result[j];
                                if (data.toLowerCase().startsWith("file:")) {
                                    data = new File(new URL(data).toURI()).toString();
                                }
                                doSomethingWithTheFile(data);
                            }
                        }
                        dtde.dropComplete(true);
                        return;
                    }
                }
            } else {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                String data = ((String) (t.getTransferData(DataFlavor.stringFlavor)));
                String[] result = data.split("\n");
                if (result != null) {
                    for (int j = 0; j < result.length; j++) {
                        data = result[j];
                        if (data.toLowerCase().startsWith("file:")) {
                            data = new File(new URL(data).toURI()).toString();
                        }
                        doSomethingWithTheFile(data);
                    }
                }
                dtde.dropComplete(true);
                return;
            }
            /*
             * drop failed
             */
            System.err.println("Drop failed: " + dtde);
            dtde.dropComplete(false);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            dtde.dropComplete(false);
        }
    }

    /**
     * just override this with your code to handle the transfered file
     */
    public void doSomethingWithTheFile(String data) {
    }
}


