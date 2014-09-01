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
package Tools;

import Commons.GenericFunctionTools;
import MyShapes.Montage;
import MyShapes.MyImage2D;
import Commons.CommonClassesLight;
import Commons.SaverLight;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * MultiThreadExecuter is just a class to multithread some time consuming tasks
 * such as image rotation
 *
 * @since <B>SF 2.3</B>
 * @author Benoit Aigouy
 */
public class MultiThreadExecuter extends GenericFunctionTools {

    public MultiThreadExecuter() {
    }

    /**
     * Constructor
     *
     * @param callableParameters parameters used by callbales
     */
    public MultiThreadExecuter(Object... callableParameters) {
        this.setCallableParameters(callableParameters);
    }

    /**
     * call method of the callable overrides the GenericFunctionTools call
     * method
     *
     * @return any object (can be null)
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        if (callableParameters.length < 2) {
            System.err.println("Multithread parameters should contain at least 2 parameters");
            return null;
        }

        /**
         * here we are going to parse options sent to the callable and do the
         * appropriate processing
         */
        String command;
        Object[] params = null;
        if (callableParameters[1] instanceof Object[]) {
            params = ((Object[]) callableParameters[1]);
            command = params[0].toString();
        } else {
            command = callableParameters[1].toString();
        }
        if (command.equals("rotateLeft")) {
            rotateLeft((MyImage2D) callableParameters[0]);
            return null;
        }
        if (command.equals("rotateRight")) {
            rotateRight((MyImage2D) callableParameters[0]);
            return null;
        }
        if (command.equals("crop")) {
            crop((MyImage2D) callableParameters[0], CommonClassesLight.String2Int(params[1]), CommonClassesLight.String2Int(params[2]), CommonClassesLight.String2Int(params[3]), CommonClassesLight.String2Int(params[4]));
            return null;
        }
        if (command.equals("rotate")) {
            rotate((MyImage2D) callableParameters[0], CommonClassesLight.String2Int(params[1]));
            return null;
        }
        if (command.equals("serialize")) {
            serialize((MyImage2D) callableParameters[0]);
            return null;
        }
        if (command.equals("reloadAfterSerialization")) {
            reloadAfterSerialization((MyImage2D) callableParameters[0]);
            return null;
        }
        if (command.equals("flip")) {
            flip((MyImage2D) callableParameters[0], params[1].toString());
            return null;
        }
        return super.call();
    }

    /*
     * Generic multithread processing of images
     * @param 1
     * @since <B>Packing Analyzer 5.0</B>
     */
    public void genericCallableWithSavingOutput(Set<Object> pos_n_shapes, Object... parameters) {
        runGarbageCollectionWhenExecuted = true;
        verbose_mode = false;
        popUpWindowWhenOutOfMemoryError = true;
        Callable[] callables = new Callable[CommonClassesLight.nb_of_processors_to_use];
        int l = 0;
        int size = pos_n_shapes.size();
        for (Object object : pos_n_shapes) {
            if (!(object instanceof MyImage2D)) {
                continue;
            }
            fireProgress(((l * 100) / size));
            if (cancel) {
                return;
            }
            callables = addCallableAndExecuteIfFull(callables, new MultiThreadExecuter(object, parameters));
            l++;
        }
        finalize(callables);
        resetCallable(callables);
    }

    /**
     * rotates an image to the left (by 90 degrees)
     *
     * @param object
     */
    public void rotateLeft(MyImage2D object) {
        object.rotateLeft();
    }

    /**
     * rotates an image to the right (by 90 degrees)
     *
     * @param object
     */
    public void rotateRight(MyImage2D object) {
        object.rotateRight();
    }

    /**
     * crops an image
     *
     * @param object
     */
    public void crop(MyImage2D object, int left, int right, int up, int down) {
        object.crop(left, right, up, down);
    }

    /**
     * rotates an image by an arbitrary angle
     *
     * @param object
     * @param angle
     */
    public void rotate(MyImage2D object, int angle) {
        ((MyImage2D) object).rotate(angle);
    }

    /**
     * Prepares an image for seialization
     *
     * @param object
     */
    public void serialize(MyImage2D object) {
        object.getTextreadyForSerialization();
    }

    /**
     * reloads an image after serialization
     *
     * @param object
     */
    public void reloadAfterSerialization(MyImage2D object) {
        object.reloadAfterSerialization();
    }

    /**
     * flips an image
     *
     * @param object
     * @param orientation
     */
    public void flip(MyImage2D object, String orientation) {
        object.flip(orientation);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //non en fait ca marche le MT marche --> tester et MT le plus de fonctions possibles
        MultiThreadExecuter test = new MultiThreadExecuter();
        //new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Text", "Progression...", 0, 100)
        test.addAvancementListener("Rotate Left");

        CommonClassesLight.setMaxNbOfProcessorsToMaxPossible();

        ArrayList<Object> shapes = new ArrayList<Object>();
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014_stretched.png"));//Series015.png//real_imageJ_image.tif
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png"));
        shapes.add(new MyShapes.MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png"));

        Montage m = new Montage(shapes, 6, 6, false, 3);

        LinkedHashSet<Object> pos_n_shapes = m.pos_n_shapes;

        long start_time = System.currentTimeMillis();
        /*
         * rotate left
         */
        test.genericCallableWithSavingOutput(pos_n_shapes, "rotateLeft");
        /*
         * rotate right
         */
        test.addAvancementListener("Rotate Right");
        test.genericCallableWithSavingOutput(pos_n_shapes, "rotateRight");
        /*
         * crop
         */
        //test.genericCallableWithSavingOutput(pos_n_shapes, "crop", 256, 256, 256, 256);//marche tres bien --> gain enorme de temps
        /*
         * rotate
         */
        //test.genericCallableWithSavingOutput(pos_n_shapes, "rotate", 45);//marche tres bien --> gain enorme de temps
        /*
         * make ready for serialization
         */
        //no obvious gain
//        test.genericCallableWithSavingOutput(pos_n_shapes, "serialize");//marche tres bien --> gain enorme de temps
        /*
         * reload after serialization
         */
//        test.genericCallableWithSavingOutput(pos_n_shapes, "reloadAfterSerialization");//marche tres bien --> gain enorme de temps
        /*
         * voir si il est possible d'accelerer le loading ???? et le saving ????? si possible car ce sont deux etapes assez longues en fait --> ca a pas l'air de prendre bcp de temps en fait meme le load est pas si long le plus long est le demarrage de R le reste est plutot facile
         */

        //faire un change journal style en MT est ce long ????
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");

        test.closeMonitor();

        m.update();
        BufferedImage tmp = new BufferedImage((int) m.getWidth(), (int) m.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = tmp.createGraphics();

        m.drawAndFill(g2d);

        g2d.dispose();
        SaverLight.popJ(tmp);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            //Logger.getLogger(MultiThreadExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.exit(0);
    }
}


