package Tools;

import Commons.CommonClassesLight;
import Commons.MyWriter;
import Commons.TextParser;
import java.util.ArrayList;

/**
 * This class adds or removes the copyright notice to/from all the classes of my
 * project
 *
 * @author Benoit Aigouy
 */
public class HandleCopyrightForAllMyClasses {

    public static final String formattedReviewLicense = "/*\n"
            + " ScientiFig/FiguR\n"
            + " Copyright (C) Benoit Aigouy 2013\n"
            + "\n"
            + " The source code of ScientiFig and FiguR is disclosed out of courtesy for\n"
            + " reviewers. It must not be made public, modified or used without the author\n"
            + " written consent. The sources must be trashed once the review is over. If\n"
            + " you do not agree with this license just delete the sources from your computer.\n"
            + " If you received this code and are not involved in the review of the Nature\n"
            + " Methods manuscript NMETH-C17170A-Z please contact benoit.aigouy@udamail.fr\n"
            + " as soon as possible.\n"
            + "\n"
            + " (this temporary license applies throughout the review of the NMETH-C17170A-Z\n"
            + " manuscript. It will be replaced by a non-copyleft open source license such as\n"
            + " new BSD license after the review)\n"
            + " */\n";
    public static final String formattedBSD = "/*\n"
            + " License ScientiFig (new BSD license)\n\n"
            + " Copyright (C) 2012-2015 Benoit Aigouy \n"
            + "\n"
            + " Redistribution and use in source and binary forms, with or without\n"
            + " modification, are permitted provided that the following conditions are\n"
            + " met:\n"
            + "\n"
            + " (1) Redistributions of source code must retain the above copyright\n"
            + " notice, this list of conditions and the following disclaimer. \n"
            + "\n"
            + " (2) Redistributions in binary form must reproduce the above copyright\n"
            + " notice, this list of conditions and the following disclaimer in\n"
            + " the documentation and/or other materials provided with the\n"
            + " distribution.  \n"
            + "    \n"
            + " (3)The name of the author may not be used to\n"
            + " endorse or promote products derived from this software without\n"
            + " specific prior written permission.\n"
            + "\n"
            + " THIS SOFTWARE IS PROVIDED BY THE AUTHOR \"AS IS\" AND ANY EXPRESS OR\n"
            + " IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n"
            + " WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n"
            + " DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,\n"
            + " INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n"
            + " (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n"
            + " SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)\n"
            + " HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,\n"
            + " STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING\n"
            + " IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE\n"
            + " POSSIBILITY OF SUCH DAMAGE.\n"
            + " */\n";

    /**
     *
     * @param txt
     * @return true if the class is copyrighted
     */
    public static boolean isCopyrighted(String txt) {
        if (txt.toLowerCase().contains("copyright")) {
            return true;
        }
        return false;
    }

    /**
     * This function can be used to remove a copyright notice from a file. It
     * can be used to change a copyright for example.
     *
     * @param txt file content
     */
    public String stripCopyrightHeader(String txt) {
        String key = "*/\n";
        int end_of_stuff = txt.indexOf(key);
        if (end_of_stuff != -1) {
            String extractedCopyright = txt.substring(0, end_of_stuff);
            /*
             * we have found a copyright --> we are going to remove it
             */
            if (extractedCopyright.toLowerCase().contains("copyright")
                    && !extractedCopyright.toLowerCase().contains("@copyright")) {
                txt = txt.substring(end_of_stuff + key.length());
                return txt;
            }
        }
        return txt;
    }

    /**
     * adds copyright to classes
     *
     * @param files
     * @param copyRight
     */
    public void addCopyRight(ArrayList<String> files, String copyRight) {
        for (String string : files) {
            String txt = TextParser.getTextAsString(string);
            if (!isCopyrighted(txt)) {
                txt = copyRight + txt;
                new MyWriter().apply(string, txt, false);
            }
        }
    }

    /**
     * removes copyright from classes
     *
     * @param files
     */
    public void stripCopyRight(ArrayList<String> files) {
        for (String string : files) {
            String txt = TextParser.getTextAsString(string);
            if (isCopyrighted(txt)) {
                new MyWriter().apply(string, stripCopyrightHeader(txt), false);
            }
        }
    }

    public static void main(String[] args) {
        
//        if (true)
//        {
//            System.out.println(formattedBSD);
//            return ;
//        }
        
        String src_folder = "C:/mon_prog/ImageJPluins/ScientiFig_/src";
        if (CommonClassesLight.isLinux()) {
            src_folder = src_folder.replaceAll("C:/", "/C/");
        }
        ArrayList<String> files = CommonClassesLight.list_whatever_full_path(src_folder, true, ".java");
        HandleCopyrightForAllMyClasses CR = new HandleCopyrightForAllMyClasses();

                /*
         * uncomment the next line to strip a copyright notice to classes
         */
        //CR.stripCopyRight(files);
        /*
         * uncomment the next line to add a copyright notice to classes.
         * Small bugs for a few classes (ScientiFig and Authorpane2) that contain the word copyright --> try to fix that some day) but just fix it manually for now
         */
        //CR.addCopyRight(files, formattedReviewLicense);
        CR.addCopyRight(files, formattedBSD);
    }
}

