package ru.everybit.bzkpd_bsk.application.service.form;

import org.camunda.bpm.engine.task.Task;
import ru.everybit.bzkpd_bsk.application.view.form.BpmForm;
import ru.everybit.bzkpd_bsk.application.view.portal.BpmWorkingArea;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BpmFormService {

    public static void showFormForTask(Task task, BpmWorkingArea workingArea) {
        BpmForm form = createForm(task);
        form.showForm(task, workingArea);
        workingArea.setContent(form.getUi());
    }

    private static BpmForm createForm(Task task) {
        try {
            Class<?> formClass;
            if (task.getFormKey() == null) {
                formClass = BpmForm.class;
            } else {
                formClass = Class.forName(task.getFormKey());
            }
            Constructor<?> formConstructor = formClass.getConstructor();
            return (BpmForm) formConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException("cannot create form class", e);
        }
    }
}
