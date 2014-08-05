/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2014 Benoit Aigouy 

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Writes strings to a file
 *
 * @since <B>Packing Analyzer 1.0</B>
 */
public class MyWriter extends GenericFunctionTools {

    /**
     * if the path contains folders which do not already exist, it creates them
     *
     * @param path path to the file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static void create_folder_if_it_does_not_exists(String path) {
        String folder_name = new File(path).getParent();
        if (folder_name != null) {
            File test_folder = new File(folder_name);
            if (!test_folder.exists()) {
                test_folder.mkdirs();
            }
        }
    }

    /**
     * Writes a String to a file (no append)
     *
     * @param output_name file to save
     * @param text text to be written
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void apply(String output_name, String text) {
        apply(output_name, text, false);
    }

    /**
     * Writes a String to a file (no append)
     *
     * @param output_name file to save
     * @param text text to be written
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void erase(String output_name, String text) {
        apply(output_name, text, false);
    }

    /**
     * Writes a String to a file (no append)
     *
     * @param output_name file to save
     * @param text text to be written
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void overWrite(String output_name, String text) {
        apply(output_name, text, false);
    }

    /**
     * Appends a String to a file
     *
     * @param output_name file to save
     * @param text text to be written
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void append(String output_name, String text) {
        apply(output_name, text, true);
    }

    /**
     * Writes an arraylist<String> to a file
     *
     * @param output_name file to save
     * @param text text to be written
     * @param append if true appends the file otherwise erases it
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void apply(String output_name, ArrayList<String> text, boolean append) {
        if (text != null && !text.isEmpty()) {
            create_folder_if_it_does_not_exists(output_name);
            try {
                FileWriter fw = new FileWriter(output_name, append);
                BufferedWriter output = new BufferedWriter(fw);
                for (int i = 0; i < text.size(); i++) {
                    output.write(text.get(i) + "\n");
                }
                output.flush();
                output.close();
                fw.close();
            } catch (IOException ioe) {
                StringWriter sw = new StringWriter();
                ioe.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }

    /**
     * Writes a String to a file
     *
     * @param output_name file to save
     * @param text text to be written
     * @param append if true appends the file otherwise erases it
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void apply(String output_name, String text, boolean append) {
        create_folder_if_it_does_not_exists(output_name);
        try {
            FileWriter fw = new FileWriter(output_name, append);
            BufferedWriter output = new BufferedWriter(fw);
            output.write(text + "\n");
            output.flush();
            output.close();
            fw.close();
        } catch (IOException ioe) {
            System.out.println("erreur : " + ioe);
        }
    }

    /**
     * Writes an arraylist<String> to a file
     *
     * @param output_name file to save
     * @param text text to write
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void apply(String output_name, ArrayList<String> text) {
        apply(output_name, text, false);
    }

    /**
     * Writes an arraylist<String> to a file (file is overwritten)
     *
     * @param output_name file to save
     * @param text text to write
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void overWrite(String output_name, ArrayList<String> text) {
        apply(output_name, text, false);
    }

    /**
     * Writes an arraylist<String> to a file (erases the file first)
     *
     * @param output_name file to save
     * @param text text to write
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void erase(String output_name, ArrayList<String> text) {
        apply(output_name, text, false);
    }

    /**
     * Appends an arraylist<String> to a file
     *
     * @param output_name file to save
     * @param text text to write
     * @since <B>Packing Analyzer 1.0</B>
     */
    public void append(String output_name, ArrayList<String> text) {
        apply(output_name, text, true);
    }

    public static void main(String[] args) {
    }
}


