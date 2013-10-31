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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.ProgressMonitor;

/**
 * GenericFunctionTools is a class that can display task progression and more
 * importantly be used to multithread tasks. It uses callables which I find much
 * more intuitive and nicer than threads and unlike threads callables can return
 * an output (and these output are collected in the same order as the input,
 * another major advantage over threads)
 *
 * @author Benoit Aigouy
 */
public class GenericFunctionTools implements Callable<Object> {

    public Object[] callableParameters = null;
    private int currentCallablePosition = 0;
    public boolean popUpWindowWhenOutOfMemoryError = false;
    public boolean runGarbageCollectionWhenExecuted = false;
    /*
     * if cancel is pressed we return
     */
    public static boolean cancel = false;
    /**
     * If true the progress will be sent to System.out
     */
    public static boolean verbose_mode = false;
    /**
     * if true diplay time when progress is fired
     */
    public boolean show_time = false;
    private ProgressMonitor progressMonitor;
    /**
     * In this array we recover all the objects produced upon execution of the
     * callables
     */
    public ArrayList<Object> collectedCallableOutput = null;

    /**
     * here we define the parameters of the function
     *
     * @param parameters
     */
    public void setCallableParameters(Object... parameters) {
        this.callableParameters = parameters;
    }

    /**
     * Converts several individual objects to an object array (can be used to
     * convert individual paramaters to an array of parameters)
     *
     * @param objects
     * @return an array of objects (parameters)
     */
    public Object[] toObjectArray(Object... objects) {
        return objects;
    }

    /**
     * Just override this to execute your own functions
     *
     * @return anything you want
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        if (callableParameters == null) {
            return null;
        }
        return null;
    }

    /**
     * Adds a user created progress monitor to the current task
     *
     * @param o can be either a string or a progressmonitor
     */
    public void addAvancementListener(Object o) {
        closeMonitor();
        if (o instanceof ProgressMonitor) {
            this.progressMonitor = (ProgressMonitor) o;
        } else if (o == null || o instanceof String) {
            String text = "Please wait...";
            if (o != null) {
                text = (String) o;
            }
            this.progressMonitor = new ProgressMonitor(CommonClassesLight.getGUIComponent(), text, "Starting soon...", 0, 100);
        }
        this.progressMonitor.setProgress(0);
        this.progressMonitor.setMillisToDecideToPopup(0);
        this.progressMonitor.setMillisToPopup(0);
        this.progressMonitor.setProgress(0);
    }

    /**
     * We have this just in case the user wants to have a max != 100 and a min
     * != 0
     *
     * @param min
     * @param max
     */
    public void setMinMax(int min, int max) {
        if (progressMonitor != null) {
            progressMonitor.setMaximum(max);
            progressMonitor.setMinimum(min);
        }
    }

    /**
     * closes the progressmonitor if exists
     */
    public void closeMonitor() {
        if (this.progressMonitor instanceof ProgressMonitor) {
            progressMonitor.close();
        }
    }

    /**
     * Automatically adds a progress monitor to the current task
     *
     */
    public void addAvancementListener() {
        addAvancementListener(null);
    }

    /**
     * Sets the progressMonitor
     *
     * @param progressMonitor
     */
    public void setAdvancementListener(ProgressMonitor progressMonitor) {
        //this.progressMonitor = progressMonitor;
        addAvancementListener(progressMonitor);
    }

    /**
     *
     * @return the progress monitor (so that it can be passed to another
     * function)
     */
    public ProgressMonitor getAdvancementListener() {
        return progressMonitor;
    }

    /**
     * update the progress
     *
     * @param progress value to display (typically a value between 0 and 100%)
     */
    public void fireProgress(int progress) {
        if (verbose_mode && progressMonitor == null) {
            if (show_time) {
                System.out.println(CommonClassesLight.create_number_of_the_appropriate_size(progress, 3) + "% " + CommonClassesLight.now("hh:mm:ss"));
            } else {
                System.out.println(progress + "%");
            }
            return;
        }
        if (progressMonitor == null) {
            return;
        }
        progressMonitor.setProgress(progress);
        progressMonitor.setNote(progress + "%");
        /**
         * we recover possible cancelation by the user
         */
        if (progressMonitor.isCanceled()) {
            if (progressMonitor.isCanceled()) {
                cancel = true;
            }
        }
    }

    /**
     * Prints an error
     *
     * @param txt
     */
    public void fireError(String txt) {
        System.err.println(txt);
    }

    /**
     * We execute the remaining callables that have not yet been executed
     *
     * @param callables
     * @return a callable array
     */
    public Callable[] finalize(Callable[] callables) {
        if (cancel) {
            return callables;
        }
        execute(callables);
        currentCallablePosition = 0;
        callables = new Callable[CommonClassesLight.nb_of_processors_to_use];
        callables = emptyCallable(callables);
        return callables;
    }

    /**
     * Add a new callable to the callable array or execute callables if array is
     * full (i.e. if there are as many callables as processors available)
     *
     * @param callables
     * @param callable
     * @return a callable array
     */
    public Callable[] addCallableAndExecuteIfFull(Callable[] callables, Callable callable) {
        if (cancel) {
            return callables;
        }
        if (currentCallablePosition >= callables.length - 1) {
            if (callable != null) {
                callables[currentCallablePosition] = callable;
            }
            finalize(callables);
            /*
             * bug fix for rerunning callables that were already ran
             */
            emptyCallable(callables);
        } else if (callable != null) {
            callables[currentCallablePosition] = callable;
            currentCallablePosition++;
        }
        return callables;
    }

    /**
     * Empties callables
     *
     * @param tmp
     * @return an empty callable array
     */
    public Callable[] emptyCallable(Callable[] tmp) {
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = null;
        }
        return tmp;
    }

    /**
     * Creates an empty callable of the desired size (ideally the size of the
     * callable should be less or equal to the number of processors)
     *
     * @param size
     * @return an empty callable array
     */
    public Callable[] createcreateEmptyCallable(int size) {
        Callable[] tmp = new Callable[size];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = null;
        }
        return tmp;
    }

    /**
     * Creates an empty callable of the desired size (ideally the size of the
     * callable should be less or equal to the number of processors)
     *
     * @param tmp
     * @param size
     * @return an empty callable array
     */
    public Callable[] createcreateEmptyCallable(Callable[] tmp, int size) {
        tmp = new Callable[size];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = null;
        }
        return tmp;
    }

    /**
     *
     * @param tmp
     * @return true if the callable array is empty (i.e. if all callables it
     * contains are null)
     */
    public boolean isEmpty(Callable[] tmp) {
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Executes all the callables contained in the callable array and stores
     * their outputs
     *
     * @param callables
     * @return a callable array
     */
    public Callable[] finalizeAndGetOutPut(Callable[] callables) {
        if (cancel) {
            return callables;
        }
        collectedCallableOutput.addAll(executeAndRecoverOutput(callables));
        currentCallablePosition = 0;
        callables = new Callable[CommonClassesLight.nb_of_processors_to_use];
        callables = emptyCallable(callables);
        return callables;
    }

    /**
     * Adds a callable to the callable array and executes all callables if the
     * callable array is full. If callables are executed their output is stored
     * in collectedCallableOutput.
     *
     * @param callables
     * @param callable
     * @return a callable array
     */
    public Callable[] addCallableOrExecuteIfFullAndSaveOutput(Callable[] callables, Callable callable) {
        if (cancel) {
            return callables;
        }
        if (collectedCallableOutput == null) {
            collectedCallableOutput = new ArrayList<Object>();
        }
        if (currentCallablePosition >= callables.length - 1) {
            if (callable != null) {
                callables[currentCallablePosition] = callable;
            }
            finalizeAndGetOutPut(callables);
            /*
             * bug fix for rerunning callables that were already ran
             */
            emptyCallable(callables);
            return callables;
        } else if (callable != null) {
            callables[currentCallablePosition] = callable;
            currentCallablePosition++;
        }
        return callables;
    }

    /**
     *
     * @return the collected callable outputs
     */
    public ArrayList<Object> getCallableOutputs() {
        return collectedCallableOutput;
    }

    /**
     * Empties collected callable outputs
     */
    public void purgeCallableOutputs() {
        if (collectedCallableOutput != null) {
            collectedCallableOutput.clear();
        }
        collectedCallableOutput = null;
    }

    /**
     * cleans callables
     */
    public void resetCallable(Callable[] callables) {
        currentCallablePosition = 0;
        /*
         * shouldn't I also empty callables here ???? probably --> check this
         */
        emptyCallable(callables);
        purgeCallableOutputs();
    }

    /**
     * This function just executes callables passed to it and returns the output
     * of the executed callables (output can be null if nothing needs to be
     * retrurned)
     *
     * @param <V>
     * @param callable callable callables to be executed
     * @return executed callable output (output is in the same order as input
     * --> less messy than what other threads do)
     */
    public <V> ArrayList<V> executeAndRecoverOutput(Callable<V>... callable) {
        ArrayList<V> cur_out = new ArrayList<V>();
        if (cancel) {
            return cur_out;
        }
        try {
            if (CommonClassesLight.isMultiThreadable() && CommonClassesLight.nb_of_processors_to_use >= 2) {
                ExecutorService es = Executors.newFixedThreadPool(CommonClassesLight.nb_of_processors_to_use);
                ArrayList<Future<V>> output = new ArrayList<Future<V>>();
                for (int l = 0; l < callable.length; l++) {
                    if (callable[l] != null) {
                        Future<V> f = es.submit(callable[l]);
                        output.add(f);
                    }
                }
                for (int l = 0; l < output.size(); l++) {
                    if (cancel) {
                        es.shutdownNow();
                        return cur_out;
                    }
                    cur_out.add(output.get(l).get());
                }
                es.shutdown();
            } else {
                /*
                 * if cannot be MultiThreaded --> just call the call method directly
                 */
                for (int l = 0; l < callable.length; l++) {
                    if (cancel) {
                        return cur_out;
                    }
                    if (callable[l] != null) {
                        cur_out.add(callable[l].call());
                    }
                }
            }
        } catch (Exception e) {
            cancel = true;
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            fireError("\nError:\n" + stacktrace);
            if (popUpWindowWhenOutOfMemoryError && stacktrace.contains("OutOfMemoryError")) {
                try {
                    System.gc();
                } catch (Exception e2) {
                }
                CommonClassesLight.Warning("Not enough memory! Please assign more memory before you re-execute this function (alternatively process les images).");
            }
        }
        return cur_out;
    }

    /**
     * This function executes callables passed to it.
     *
     * @param callable callables to be executed
     */
    public <T> void execute(Callable<T>... callable) {
        if (cancel) {
            return;
        }
        try {
            if (CommonClassesLight.isMultiThreadable() && CommonClassesLight.nb_of_processors_to_use >= 2) {
                ExecutorService es = Executors.newFixedThreadPool(CommonClassesLight.nb_of_processors_to_use);
                ArrayList<Future<T>> output = new ArrayList<Future<T>>();
                for (int l = 0; l < callable.length; l++) {
                    if (callable[l] != null) {
                        Future<T> f = es.submit(callable[l]);
                        output.add(f);
                    }
                }
                for (int l = 0; l < output.size(); l++) {
                    if (cancel) {
                        es.shutdownNow();
                        return;
                    }
                    try {
                        output.get(l).get();
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        fireError("\nError:\n" + stacktrace);
                    }
                }
                es.shutdown();
            } else {
                /*
                 * if cannot be MultiThreaded --> just call the call method directly
                 */
                for (int l = 0; l < callable.length; l++) {
                    if (cancel) {
                        return;
                    }
                    try {
                        if (callable[l] != null) {
                            callable[l].call();
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        fireError("\nError:\n" + stacktrace);
                    }
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            fireError("\nError:\n" + stacktrace);
        }
        if (runGarbageCollectionWhenExecuted) {
            try {
                System.gc();
            } catch (Exception e) {
            }
        }
    }
}


